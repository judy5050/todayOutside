package ga.todayOutside.src.messageBoard;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.messageBoard.models.PostMessageBoardReq;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        messageBoard=new MessageBoard(userInfo,addressName,postMessageBoardReq.getMsg(),0L);
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
}
