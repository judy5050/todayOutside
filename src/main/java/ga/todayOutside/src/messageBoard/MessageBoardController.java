package ga.todayOutside.src.messageBoard;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.messageBoard.models.PatchMessageBoardReq;
import ga.todayOutside.src.messageBoard.models.PostMessageBoardReq;
import ga.todayOutside.src.messageBoard.models.PostMessageBoardRes;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageBoardController {

    private final JwtService jwtService;
    private final MessageBoardService messageBoardService;
    private final UserInfoService userInfoService;
    private final AddressService addressService;


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

                 messageBoard = messageBoardService.postMessageBoard(userInfo, addressMsg, postMessageBoardReq);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

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

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_MESSAGE_BOARD);


    }



    /**
     * 메시지 찾기
     */
    @ResponseBody
    @GetMapping("/test/{messgeIdx}")
    public BaseResponse<Void> test(@PathVariable Long messgeIdx){

        messageBoardService.findByMessage(messgeIdx);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

}
