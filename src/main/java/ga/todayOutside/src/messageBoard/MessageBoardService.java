package ga.todayOutside.src.messageBoard;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.GetAddressRes;
import ga.todayOutside.src.messageBoard.models.*;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.src.weather.WeatherService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageBoardService {

    private final MessageBoardRepository messageBoardRepository;
    private final UserInfoService userInfoService;


        public MessageBoard getMessageBoard(Long messageBoardIdx) throws BaseException {
            MessageBoard messageBoard = messageBoardRepository.findById(messageBoardIdx).orElse(null);
            if(messageBoard==null){

                throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BOARD);//TODO 실패코드 변경하기
            }
            return messageBoard;
        }



//
//    address=new Address(userInfo,postAddressReq.getFirstAddressName(), postAddressReq.getSecondAddressName(),1);
//            System.out.println("address = " + address.getFirstAddressName());
//            addressRepository.save(address);

    @Transactional
    public MessageBoard postMessageBoard(UserInfo userInfo, String addressName, PostMessageBoardReq postMessageBoardReq) throws BaseException {
        MessageBoard messageBoard=null;
        if(postMessageBoardReq.getMsg()==null||postMessageBoardReq.getMsg().isEmpty()){
            throw new BaseException(BaseResponseStatus.EMPTY_MESSAGE_BOARD);
        }
        messageBoard=new MessageBoard(userInfo,addressName,postMessageBoardReq.getMsg(),0L,postMessageBoardReq.getBoardType());
        messageBoardRepository.save(messageBoard);

        return messageBoard;
    }

    /**
     *
     * 메시지 조회
     */
    //TODO 해당 코드 나중에 변경하기
    public MessageBoard findByMessage(Long messageIdx) throws BaseException {
        MessageBoard  messageBoard = messageBoardRepository.findById(messageIdx).orElse(null);
        System.out.println("messageBoard.getIsDeleted() = " + messageBoard.getIsDeleted());
        if(messageBoard==null||messageBoard.getIsDeleted().equals("Y")){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BOARD);
        }

        return messageBoard;

    }

    /**
     *회원인덱스로 관련된 게시글 찾기
     */
    public MessageBoard findMessageBoardByUserIdx(Long userIdx,Long messageBoardIdx) throws BaseException {


     MessageBoard messageBoard = messageBoardRepository.findById(messageBoardIdx).orElse(null);

     if(messageBoard==null){
         throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BOARD);
     }
     else {
         //자기의 게시글이 아니라면
         if(messageBoard.getUserInfo().getId()!=userIdx){
             throw new BaseException(BaseResponseStatus.NOT_MATCH_USER_MESSAGE_BOARD);
         }

     }
     return messageBoard;


    }

    /**
     * 메시지 인덱스로 찾기
     */
    public MessageBoard findMessageBoardByIdx(Long messageBoardIdx) throws BaseException {

        MessageBoard messageBoard = messageBoardRepository.findById(messageBoardIdx).orElse(null);

        if(messageBoard==null){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BOARD);
        }



        return messageBoard;
    }

    /**
     * 저장
     */
    @Transactional
    public void save(MessageBoard messageBoard) {
        messageBoardRepository.save(messageBoard);
    }

    /**
     * 삭제
     */
    @Transactional
    public void delete(MessageBoard messageBoard){

        messageBoardRepository.delete(messageBoard);
    }

    /**
     *하트순서대로 날씨 관련 게시글 리스트 가져오기
     */
    public List<GetMessageBoardRecentlyRes> getHeartMessageBoardList(String secondAddressName, int page, BoardType boardType) {

        int index;

        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes;

        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date ;

        //page 처리
        PageRequest pageRequest=PageRequest.of(page,10);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressMsgLike(filter,pageRequest,boardType,"N");
        List<GetMessageBoardRecentlyRes> getMessageBoardHeartRes1=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();

        return getMessageBoardHeartRes1;
    }


    /**
     * 날씨 게시글 최신 순 조회
     */
    public List<GetMessageBoardRecentlyRes> getRecentlyMessageBoardList(String secondAddressName, int page, BoardType boardType) {

        int index;
        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes;

        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        //page 처리
        PageRequest pageRequest=PageRequest.of(page,10);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressRecentlyMsg(filter,pageRequest,boardType,"N");
        List<GetMessageBoardRecentlyRes> getMessageBoardHeartRes1=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();

        return getMessageBoardHeartRes1;
    }
    /**
     * 날씨  게시글 최신순 한개 조회
     */

    public List<GetMessageBoardRecentlyRes> getRecentlyTop1(String secondAddressName) {

        int index;
        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes=null;
        GetMessageBoardRecentlyRes messageBoardRecentlyRes=null;
        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        PageRequest pageRequest=PageRequest.of(0,1);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressRecentlyMsg(filter,pageRequest,BoardType.WEATHER,"N");
        getMessageBoardRecentlyRes=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();
        if(getMessageBoardRecentlyRes.isEmpty()){
            System.out.println("데이터 없음");
//            messageBoardRecentlyRes=new GetMessageBoardRecentlyRes("Y");
//            System.out.println("messageBoardRecentlyRes.getIsExistent() = " + messageBoardRecentlyRes.getIsExistent());
//            getMessageBoardRecentlyRes.add(messageBoardRecentlyRes);
        }



//        System.out.println("getMessageBoardHeartRes1 = " + getMessageBoardHeartRes1);
        return getMessageBoardRecentlyRes;
    }

    /**
     * 재난 관련 게시글 최신순 한개 조회
     */

    public List<GetMessageBoardRecentlyRes> getRecentlyTop1DisasterTalk(String secondAddressName) {

        int index;
        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes=null;
        GetMessageBoardRecentlyRes messageBoardRecentlyRes=null;
        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        PageRequest pageRequest=PageRequest.of(0,1);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressRecentlyMsg(filter,pageRequest,BoardType.DISASTER,"N");
        getMessageBoardRecentlyRes=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();
        if(getMessageBoardRecentlyRes.isEmpty()){
            System.out.println("데이터 없음");
//            messageBoardRecentlyRes=new GetMessageBoardRecentlyRes("Y");
//            System.out.println("messageBoardRecentlyRes.getIsExistent() = " + messageBoardRecentlyRes.getIsExistent());
//            getMessageBoardRecentlyRes.add(messageBoardRecentlyRes);
        }



//        System.out.println("getMessageBoardHeartRes1 = " + getMessageBoardHeartRes1);
        return getMessageBoardRecentlyRes;
    }


    /**
     * 재난 관련 하트순
     */

    public List<GetMessageBoardRecentlyRes> getHeartMessageBoardDisasterList(String secondAddressName, int page, BoardType boardType) {

        int index;
        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes;

        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date ;

        //page 처리
        PageRequest pageRequest=PageRequest.of(page,10);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressMsgLike(filter,pageRequest,boardType,"N");
        List<GetMessageBoardRecentlyRes> getMessageBoardListHeartRes=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();

        return getMessageBoardListHeartRes;
    }

    /**
     * 재난 관련 최신순
     */

    public List<GetMessageBoardRecentlyRes> getRecentlyMessageBoardDisasterList(String secondAddressName, int page, BoardType boardType) {

        int index;
        String filter;
        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes;

        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            filter=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            filter=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }
        System.out.println("filter = " + filter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date ;

        //page 처리
        PageRequest pageRequest=PageRequest.of(page,10);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressRecentlyMsg(filter,pageRequest,boardType,"N");
        List<GetMessageBoardRecentlyRes> getMessageBoardListHeartRes=messageBoards.map(GetMessageBoardRecentlyRes::new).getContent();

        return getMessageBoardListHeartRes;
    }


    /**
     * 하트 수 증가
     */
    @Transactional
    public void updateHeartNumPlus(Long messageBoardIdx) {

        System.out.println("messageBoardIdx = " + messageBoardIdx);
        messageBoardRepository.setHeartNumPlus(messageBoardIdx);



    }

    /**
     * 하트 수 감소
     */
    @Transactional
    public void updateHeartNumSub(Long messageBoardIdx){
        System.out.println("messageBoardIdx = " + messageBoardIdx);
        messageBoardRepository.setHeartNumSub(messageBoardIdx);
    }

    /**
     * 작성한 모든 게시물 보기
     */
    public List<GetMyMessageListRes> findMessageAllByUserIdx(Long userIdx,String page) throws BaseException {

        PageRequest pageRequest=PageRequest.of(Integer.parseInt(page),10);
        Page<MessageBoard> messageBoardList = messageBoardRepository.findByUserIdx(userIdx, pageRequest);
        if(messageBoardList.isEmpty()||messageBoardList.getContent()==null){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BY_USERS);
        }
        List<GetMyMessageListRes> myMessageListRes=messageBoardList.map(GetMyMessageListRes::new).getContent();


            return  myMessageListRes;
    }

    /**
     * 게시글 등록한 유저 찾기
     */
    public UserInfo findByUser(MessageBoard messageBoard) throws BaseException {
        UserInfo userInfo = userInfoService.findByUserIdx(messageBoard.getUserInfo().getId());
        return userInfo;
    }
    public String todayDate(){

        Calendar calendar;
        SimpleDateFormat sdf;
        String todayStr;
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        sdf = new SimpleDateFormat("yyyyMMdd");
        todayStr = sdf.format(calendar.getTime());

        return todayStr;
    }
}
