package ga.todayOutside.src.messageBoard;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.messageBoard.models.*;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageBoardService {

    private final MessageBoardRepository messageBoardRepository;
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

    //TODO 해당 코드 나중에 변경하기
    public void findByMessage(Long messgeIdx) {
        Optional<MessageBoard> byId = messageBoardRepository.findById(messgeIdx);
        System.out.println("byId.get() = " + byId.get().getMessage());
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
     *구 단위로 게시글 리스트 가져오기
     */
    public List<GetMessageBoardHeartRes> getRecentlyMessageBoardList(String secondAddressName, String page, BoardType boardType) {

        int index;
        String filter;
        List<GetMessageBoardHeartRes>getMessageBoardHeartRes;

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
        PageRequest pageRequest=PageRequest.of(Integer.parseInt(page),10);
        Page <MessageBoard> messageBoards= messageBoardRepository.findByAddressMsgLike(filter,pageRequest,boardType);
        List<GetMessageBoardHeartRes> getMessageBoardHeartRes1=messageBoards.map(GetMessageBoardHeartRes::new).getContent();


//        for(int i=0;i<messageBoards.getContent().size();i++){
//
//            System.out.println("messageBoards = " + messageBoards.getContent().get(i).getAddressMsg());
////            System.out.println("messageBoards = " + messageBoards.getContent().get(i).getUpdatedAt());
//             date = simpleDateFormat.format(messageBoards.getContent().get(i).getCreatedAt());
//             System.out.println("date = " + date);
//
//
//        }
//        System.out.println("getMessageBoardHeartRes1 = " + getMessageBoardHeartRes1);
        return getMessageBoardHeartRes1;
    }
}