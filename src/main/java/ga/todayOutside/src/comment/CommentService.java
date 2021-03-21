package ga.todayOutside.src.comment;



import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.comment.model.GetCommentRes;
import ga.todayOutside.src.comment.model.PostCommentReq;
import ga.todayOutside.src.comment.model.PostCommentRes;
import ga.todayOutside.src.messageBoard.MessageBoardRepository;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private  final CommentRepository commentRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final MessageBoardRepository messageBoardRepository;

    @Transactional
    public List<GetCommentRes> findAllByMessageId(Long messageBoardIdx,String page) throws BaseException {

        PageRequest pageRequest=PageRequest.of(Integer.parseInt(page),10);
        Page<Comment> comments = commentRepository.findAllByMessageId(messageBoardIdx, pageRequest);
        List<GetCommentRes> getCommentRes=comments.map(GetCommentRes::new).getContent();
        if(getCommentRes.isEmpty()){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);// 실패코트 값 변경하
        }

        return getCommentRes;
    }

    /**
     * db 댓글 등록
     * @return
     */
    @Transactional
    public PostCommentRes postComments(PostCommentReq postCommentReq, Long boardIdx) throws BaseException {

        Long userId = postCommentReq.getUserIdx();
        String commentMsg = postCommentReq.getCommentMsg();
        String address = postCommentReq.getAddressMsg();
        UserInfo userInfo = null;
        MessageBoard messageBoard = null;

        // 유저 존재 여부 로직
        try {
            userInfo = userInfoRepository.findById(userId).orElse(null);

        } catch (Exception ignore) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }

        if (userInfo == null) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        //게시글 존재 여부 로직
        try {
            messageBoard = messageBoardRepository.findById(boardIdx).orElse(null);
        } catch(Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_COMMENTS);
        }

        if (messageBoard == null) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE_BOARD);
        }

        Comment comment = Comment.builder()
                .userInfo(userInfo)
                .addressMsg(address)
                .commentMsg(commentMsg)
                .messageBoard(messageBoard)
                .build();

        commentRepository.save(comment);

        return new PostCommentRes(userId, boardIdx, commentMsg);
    }
}
