package ga.todayOutside.src.comment;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.comment.model.*;
import ga.todayOutside.src.messageBoard.models.GetMessageBoardRecentlyRes;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CommentController {


    private final JwtService jwtService;

    private final UserInfoService userInfoService;

    private final CommentService commentService;

    /**
     * 게시글과 관련된 댓글 조회
     */
    @GetMapping("/messageBoards/{messageBoardIdx}/commentList")
    public BaseResponse<GetCommentListRes> getCommentList(@PathVariable Long messageBoardIdx, @RequestParam("page")int page){

        Long userIdx;
        Address address;
        UserInfo userInfo;
        GetCommentListRes commentListRes;
        try {
            //jwt 토큰값으로 유저 확인
            userIdx = jwtService.getUserId();
            //해당 유저 존재하는지 확인
            userInfo=userInfoService.findByUserIdx(userIdx);

            commentListRes = commentService.findAllByMessageId(messageBoardIdx, page);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_COMMENT_LIST,commentListRes);
    }

    /**
     * 댓글 등록
     * @param req
     * @param messageBoardIdx
     * @return
     * @throws BaseException
     */
    @PostMapping("/messageBoards/{messageBoardIdx}/comment")
    public BaseResponse<Void> postComment(@RequestBody PostCommentReq req, @PathVariable Long messageBoardIdx) {
        //구 동 띄어쓰기로 입력


        if (req.getThirdAddress().equals("")) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_TOWN_INFO);
        }

        try {
            PostCommentRes result = commentService.postComments(req, messageBoardIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_COMMENTS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 댓글 삭제
     * @param userIdx
     * @param messageBoardIdx
     * @param commentIdx
     * @return
     */
    @DeleteMapping("/messageBoards/{messageBoardIdx}/comment/{commentIdx}")
    public BaseResponse<Void> deleteComments(@RequestParam Long userIdx, @PathVariable Long messageBoardIdx,
                                             @PathVariable Long commentIdx) {

        try {

            UserInfo userInfo = commentService.deleteComment(userIdx, commentIdx);
            userInfoService.updateUserTalkSub(userInfo);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_COMMENTS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 내 댓글 조회
     */
    @GetMapping("/comments")
    public BaseResponse<List<GetCommentRes>> getMyComments(@RequestParam Long userId, @RequestParam int start) {

        try {
            List<GetCommentRes> getCommentRes = commentService.getMyComments(userId, start);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_COMMENTS, getCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 내 댓글단 게시글 조회
     */

    @GetMapping("/comments/board")
    public BaseResponse<JSONObject> getMyCommentBoard(@RequestParam Long userId, @RequestParam int start) {

        try {
            JSONObject getMessageBoardRecentlyRes = commentService.getMyCommentBoards(userId, start);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_COMMENTS, getMessageBoardRecentlyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
