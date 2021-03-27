package ga.todayOutside.src.comment;



import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.comment.model.GetCommentRes;
import ga.todayOutside.src.comment.model.PostCommentReq;
import ga.todayOutside.src.comment.model.PostCommentRes;
import ga.todayOutside.src.messageBoard.MessageBoardRepository;
import ga.todayOutside.src.messageBoard.models.GetMessageBoardRecentlyRes;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private  final CommentRepository commentRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final MessageBoardRepository messageBoardRepository;
    @Autowired
    private final JwtService jwtService;

    @Transactional
    public List<GetCommentRes> findAllByMessageId(Long messageBoardIdx,int page) throws BaseException {

        PageRequest pageRequest=PageRequest.of(page,10);
        Page<Comment> comments = commentRepository.findAllByMessageId(messageBoardIdx, pageRequest,"N");
        List<GetCommentRes> getCommentRes=comments.map(GetCommentRes::new).getContent();
        if(getCommentRes.isEmpty()){
            throw new BaseException(BaseResponseStatus.NOU_FOUND_COMMENT);
        }

        return getCommentRes;
    }

    /**
     * 댓글 등록
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
        userInfoRepository.updateUserTalkPlus(userInfo.getId());

        return new PostCommentRes(userId, boardIdx, commentMsg);
    }

    /**
     * 댓글 삭제
     */
    public UserInfo deleteComment(Long userIdx, Long commentIdx) throws BaseException {

        Comment comment = null;
        UserInfo userInfo = null;

        try {
            comment = commentRepository.findById(commentIdx).orElse(null);
            userInfo = comment.getUserInfo();
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_COMMENTS);
        }

        if (comment == null) {
            throw new BaseException(BaseResponseStatus.EMPTY_MESSAGE_COMMENTS);
        }

        if (comment.getUserInfo().getId() != userIdx) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_DELETE_COMMENTS);
        }

        commentRepository.delete(comment);

        return userInfo;
    }

    /**
     * 내 댓글 조회
     */
    public List<GetCommentRes> getMyComments(Long userIdx, int start) throws BaseException {

        Page<Comment> comment = null;

        try {
            PageRequest pageRequest = PageRequest.of(start, 10);
            comment = commentRepository.findAllByUserIdx(userIdx, pageRequest,"N");

        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_COMMENTS);
        }

        List<GetCommentRes> getCommentRes = comment.map(GetCommentRes::new).getContent();
        return getCommentRes;
    }

    /**
     * 내가 댓글단 게시글 조
     * @param userIdx
     * @param start
     * @return
     * @throws BaseException
     */
    public JSONObject getMyCommentBoards(Long userIdx, int start) throws BaseException {

        JSONObject result = new JSONObject();
        Page<Comment> comment = null;
        HashSet<Long> index = new HashSet<>();
        int total = 0;

        try {
            PageRequest pageRequest = PageRequest.of(start, 10);
            comment = commentRepository.findAllByUserIdx(userIdx, pageRequest,"N");

        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_COMMENTS);
        }

        List<GetMessageBoardRecentlyRes> getMessageBoardRecentlyRes = comment
                .map(Comment::getMessageBoard)
                .map(GetMessageBoardRecentlyRes::new)
                .getContent();

        total = getMessageBoardRecentlyRes.size();

        //중복 게시글 필터링
        getMessageBoardRecentlyRes = getMessageBoardRecentlyRes
                .stream().filter( a -> {
                    if (index.contains(a.getMessageBoardIdx())) {
                        return false;
                    }

                    index.add(a.getMessageBoardIdx());
                    return true;
                }).collect(Collectors.toList());

        result.put("total", total);
        result.put("boards", getMessageBoardRecentlyRes);

        return result;
    }

    /**
     * 댓글 반환
     * @param commentIdx
     * @return
     */
    public Comment getComments(Long commentIdx) throws BaseException {

        Comment comment = commentRepository.findById(commentIdx).orElse(null);
        if(comment==null){

            throw new BaseException(BaseResponseStatus.EMPTY_COMMENT);
        }



        return comment;
    }

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);

    }
}
