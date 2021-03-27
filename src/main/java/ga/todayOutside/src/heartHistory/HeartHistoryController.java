package ga.todayOutside.src.heartHistory;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.messageBoard.MessageBoardService;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartHistoryController {


    private  final JwtService jwtService;
    private final UserInfoService userInfoService;
    private final  HeartHistoryService heartHistoryService;
    private  final  MessageBoardService messageBoardService;
    /**
     * 게시글 하트 누르기
     */






}
