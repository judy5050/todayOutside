package ga.todayOutside.src.messageBoard;


import com.fasterxml.jackson.databind.ser.Serializers;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.AddressRepository;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.heartHistory.HeartHistoryService;
import ga.todayOutside.src.heartHistory.model.HeartHistory;
import ga.todayOutside.src.heartHistory.model.PostHeartRes;
import ga.todayOutside.src.messageBoard.models.*;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageBoardController {

    private final JwtService jwtService;
    private final MessageBoardService messageBoardService;
    private final UserInfoService userInfoService;
    private final AddressService addressService;
    private final HeartHistoryService heartHistoryService;
    private final AddressRepository addressRepository;
    /**
     * 게시글 등록
     */
    @ResponseBody
    @PostMapping("/address/{addressIdx}/messageBoards")
    public BaseResponse<PostMessageBoardRes> postMessageBoard(@PathVariable Long addressIdx, @RequestBody PostMessageBoardReq postMessageBoardReq){
        Long userIdx;
        Address address=null;
        UserInfo userInfo=null;
        String addressMsg=" ";
        MessageBoard messageBoard;
        String addressMdl=" ";
        try {
            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
            address = addressService.findByAddress(addressIdx, userIdx);
            int index;
            //동정보 저장 안된경우 동 정보를 저장하세요!
            if(address.getThirdAddressName()==null||address.getThirdAddressName().isEmpty()){

                return new BaseResponse<>(BaseResponseStatus.EMPTY_THIRD_ADDRESS);
            }

            //구와 동만 잘라서 보내는 곳
            //시나 구나 이런게 한가지만 들어가있을경우 구나 시 정보 +동정보 반환
            //TODO 서비스 로직으로 넘기기 (개발 얼른 끝내고 코드 수정하기)
            else{

                if(address.getSecondAddressName().length()>=2&&address.getSecondAddressName().length()<=4){
                    addressMsg=address.getThirdAddressName()+" "+address.getThirdAddressName();
                }
                //시 구 두개 다 합쳐있을경우 구 정보만 자르고 +동정보 반환
                else{
                    index=address.getSecondAddressName().indexOf("시");
                    addressMdl=address.getSecondAddressName().substring(index+1);
                    System.out.println("index = " + index);
                    addressMsg=addressMdl+" "+address.getThirdAddressName();
                }

                 messageBoard = messageBoardService.postMessageBoard(userInfo, addressMdl, postMessageBoardReq);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        //유저 talkNum 증가
        userInfoService.updateUserTalk(userInfo);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_MESSAGE_BOARD,new PostMessageBoardRes(messageBoard.getId()));

    }

    /**
     * 게시글 수정
     */
    @ResponseBody
    @PatchMapping("/messageBoards/{messageBoardIdx}")
    public BaseResponse<Void> patchMessageBoard(@PathVariable Long messageBoardIdx, @RequestBody PatchMessageBoardReq patchMessageBoardReq){

        Long userIdx;
        UserInfo userInfo=null;
        MessageBoard messageBoard;
        try {
            userIdx = jwtService.getUserId();

            //존재하는 회원인지 확인 후
            userInfo = userInfoService.findByUserIdx(userIdx);
            //수정하고자 하는 messageBoard가 자기 글인지 확인
            messageBoard = messageBoardService.findMessageBoardByUserIdx(userIdx, messageBoardIdx);

            if(patchMessageBoardReq.getMsg()==null||patchMessageBoardReq.getMsg().isEmpty()){
                throw new BaseException(BaseResponseStatus.EMPTY_MESSAGE_BOARD); 
            }

            messageBoard.setMessage(patchMessageBoardReq.getMsg());
            messageBoardService.save(messageBoard);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }



        return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_MESSAGE_BOARD);
    }

    /**
     * 게시판 글 삭제
     */
    @ResponseBody
    @DeleteMapping("/messageBoards/{messageBoardIdx}")
    public BaseResponse<Void> deleteMessageBoard(@PathVariable Long messageBoardIdx){

        Long userIdx;
        UserInfo userInfo=null;
        MessageBoard messageBoard;
        try {
            userIdx = jwtService.getUserId();

            //존재하는 회원인지 확인 후
            userInfo = userInfoService.findByUserIdx(userIdx);
            //수정하고자 하는 messageBoard가 자기 글인지 확인
            messageBoard = messageBoardService.findMessageBoardByUserIdx(userIdx, messageBoardIdx);
            messageBoardService.delete(messageBoard);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        userInfoService.updateUserTalkSub(userInfo);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_MESSAGE_BOARD);


    }

    /**
     * 게시판 글 조회 API
     * boardType가 heat일 경우 heart순 정렬
     * recently 일 경우 최신순 정렬
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/messageBoardList")
    public BaseResponse<List<GetMessageBoardRecentlyRes>>getMessageBoardList(@PathVariable Long addressIdx, @RequestParam("sortType")String sortType, @RequestParam("boardType") BoardType boardType, @RequestParam("page")String page){

        Long userIdx;
        Address address=null;
        UserInfo userInfo=null;
        String addressMsg=" ";
        MessageBoard messageBoard;
        String addressMdl=" ";
        try {
            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
            address = addressService.findByAddress(addressIdx, userIdx);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }


        //하트순 조회
        if(sortType.equals("heart")&&boardType.equals(BoardType.WEATHER)){

            List<GetMessageBoardRecentlyRes> recentlyMessageBoardList = messageBoardService.getHeartMessageBoardList(address.getSecondAddressName(), page,boardType);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD_HEART,recentlyMessageBoardList);

        }

        //날씨 최신순 조회
        else if(sortType.equals("recently")&&boardType.equals(BoardType.WEATHER)){

            List<GetMessageBoardRecentlyRes> recentlyMessageBoardList = messageBoardService.getRecentlyMessageBoardList(address.getSecondAddressName(), page,boardType);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD_RECENTLY,recentlyMessageBoardList);


        }

        else if(sortType.equals("heart")&&boardType.equals(BoardType.DISASTER)){

            List<GetMessageBoardRecentlyRes> recentlyMessageBoardList = messageBoardService.getHeartMessageBoardDisasterList(address.getSecondAddressName(), page,boardType);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD_HEART,recentlyMessageBoardList);


        }
        else if(sortType.equals("recently")&&boardType.equals(BoardType.DISASTER)){

            List<GetMessageBoardRecentlyRes> recentlyMessageBoardList = messageBoardService.getRecentlyMessageBoardDisasterList(address.getSecondAddressName(), page,boardType);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD_RECENTLY,recentlyMessageBoardList);



        }
        else if(sortType.isEmpty()||sortType==null){
            return new BaseResponse<>(BaseResponseStatus.FAILED_TO_POST_ADDRESS);//TODO 오류 값 수정하기 boardType를 입력하세요
        }


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD_HEART);
    }


    /**
     * 게시글 보기
     */
    @ResponseBody
    @GetMapping("/messageBoards/{messageBoardIdx}")
    public BaseResponse<GetMessageBoardReq> test(@PathVariable Long messageBoardIdx) throws BaseException {
        UserInfo userInfo;
        Long userIdx;
        MessageBoard messageBoard;
        try {
            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
             messageBoard = messageBoardService.findByMessage(messageBoardIdx);

        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD,new GetMessageBoardReq(messageBoard));
    }

    /**
     *하트 등록
     *
     * */
    @ResponseBody
    @GetMapping("/messageBoards/{messageBoardIdx}/heart")
    public BaseResponse<PostHeartRes> postHeart(@PathVariable Long messageBoardIdx){
        Long userIdx;
        UserInfo userInfo;
        MessageBoard messageBoard;
        HeartHistory heartHistory;
        MessageBoard messageBoard1;
        try {
            userIdx = jwtService.getUserId();

            userInfo = userInfoService.findByUserIdx(userIdx);

            System.out.println("userInfo = " + userInfo);
            messageBoard= messageBoardService.getMessageBoard(messageBoardIdx);

            System.out.println("messageBoard = " + messageBoard);

             heartHistory = heartHistoryService.postHeart(messageBoard, userInfo);
             messageBoard1=messageBoardService.getMessageBoard(messageBoardIdx);

        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }



        return new BaseResponse(BaseResponseStatus.SUCCESS_POST_HEART,new PostHeartRes(messageBoard1));
    }

    @ResponseBody
    @GetMapping("/homeMessageBoard")
    public BaseResponse<ArrayList> homeTalk(){

        Long userIdx;
        UserInfo userInfo;
        MessageBoard messageBoard;
        HeartHistory heartHistory;
        MessageBoard messageBoard1;
        List<Address> address=null;
        ArrayList arrayList=new ArrayList();
        try {
            //유저 정보 받기
            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
            address = addressRepository.findByUserAddress(userIdx);
            if(address.size()==0){
                throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);
            }
            else{
                for(int i=0;i<address.size();i++){
                    System.out.println("i = " + i);
                    address.get(i).getId();
                    arrayList.addAll( messageBoardService.getRecentlyTop1(address.get(i).getSecondAddressName()));
                    if(arrayList.size()-1!=i){
                        arrayList.add(new GetMessageBoardRecentlyRes("N"));

                    }
                }

            }




        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return  new BaseResponse<>(BaseResponseStatus.SUCCESS_HOME_WEATHER_MESSAGE,arrayList);


    }

    @ResponseBody
    @GetMapping("/timeTest")
    public  String time(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        String date = simpleDateFormat.format(new Date());
        return date;
    }




}
