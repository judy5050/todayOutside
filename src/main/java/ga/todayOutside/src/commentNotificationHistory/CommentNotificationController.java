package ga.todayOutside.src.commentNotificationHistory;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.comment.CommentService;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.commentNotificationHistory.model.CommentNotificationHistory;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.notificationHistory.NotificationRepository;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class CommentNotificationController {

    private final JwtService jwtService;
    private final UserInfoService userInfoService;
    private final CommentService commentService;
    private final CommentNotificationRepository commentNotificationRepository ;

    /**
     * 댓글 신고
     */
    @ResponseBody
    @PostMapping("/comments/{commentIdx}/notification")
    public BaseResponse<Void> postCommentNotification(@PathVariable Long commentIdx){

        Long userIdx;
        UserInfo userInfo=null;
        Comment comment;

        try {
            userIdx = jwtService.getUserId();

            //존재하는 회원인지 확인 후
            userInfo = userInfoService.findByUserIdx(userIdx);

            //해당 댓글이 존재하는지 확인 ,존재 후 반환
            comment=commentService.getComments(commentIdx);
            //이미 신고한 기록이 있는지 확인
            List<CommentNotificationHistory> notificationHistories = commentNotificationRepository.findByUserIdxAndCommentIdx(userIdx, comment.getId());



            if(comment.getUserInfo().getId()==userIdx){

                return new BaseResponse<>(BaseResponseStatus.NOT_POST_MY_COMMENT_NOTIFICATION);
            }

            if(!notificationHistories.isEmpty()){

                return new BaseResponse<>(BaseResponseStatus.ALREADY_POST_NOTIFICATION_COMMENT);
            }



            //게시글 신고
            commentNotificationRepository.save(new CommentNotificationHistory(comment,userInfo));



        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }






        return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_NOTIFICATION_COMMENT);
    }

}
