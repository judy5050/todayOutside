package ga.todayOutside.src.notificationHistory;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.messageBoard.MessageBoardRepository;
import ga.todayOutside.src.messageBoard.MessageBoardService;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final JwtService jwtService;
    private final UserInfoService userInfoService;
    private  final MessageBoardService messageBoardService;
    private final NotificationRepository notificationRepository;
    /**
     * 신고 하기
     */

    @ResponseBody
    @PostMapping("/messageBoards/{messageBoardIdx}/notification")
    public BaseResponse<Void>postNotification(@PathVariable("messageBoardIdx")Long messageBoardIdx){

        Long userIdx;
        UserInfo userInfo=null;
        MessageBoard messageBoard;


        try {
            userIdx = jwtService.getUserId();

            //존재하는 회원인지 확인 후
            userInfo = userInfoService.findByUserIdx(userIdx);
            System.out.println("messageBoardIdx = " + messageBoardIdx);
            //해당 게시글이 존재하는지 확인 ,존재 후 반환
            messageBoard = messageBoardService.findMessageBoardByIdx(messageBoardIdx);
            //이미 신고한 기록이 있는지 확인
            List<NotificationHistory> byUserIdxAndMessageBoardIdx = notificationRepository.findByUserIdxAndMessageBoardIdx(userIdx, messageBoardIdx);


            if(messageBoard.getUserInfo().getId()==userIdx){

                return new BaseResponse<>(BaseResponseStatus.NOT_POST_MY_MESSAGE_BOARD_NOTIFICATION);
            }

            if(!byUserIdxAndMessageBoardIdx.isEmpty()){

                return new BaseResponse<>(BaseResponseStatus.ALREADY_POST_NOTIFICATION);
            }


            //게시글 신고
            notificationRepository.save(new NotificationHistory(messageBoard,userInfo));
            int count = notificationRepository.findByMessageBoardIdx(messageBoard.getId());
            //게시글 신고수 5개 넘을경우 게시글 상태 바꿈
            if(count>=5){
                messageBoard.setIsDeleted("Y");
                messageBoardService.save(messageBoard);
            }
            System.out.println("count = " + count);


        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }




        return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_NOTIFICATION);
    }



}
