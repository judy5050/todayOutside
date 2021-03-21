package ga.todayOutside.src.comment;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.comment.model.GetCommentRes;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    @ResponseBody
    @GetMapping("/messageBoards/{messageBoardIdx}/commentList")
    public BaseResponse<List<GetCommentRes>> getCommentList(@PathVariable Long messageBoardIdx, @RequestParam("page")String page){

        Long userIdx;
        Address address;
        UserInfo userInfo;
        List<GetCommentRes> commentRes;
        try {
            //jwt 토큰값으로 유저 확인
            userIdx = jwtService.getUserId();
            //해당 유저 존재하는지 확인
//            userInfo=userInfoService.findByUserIdx(userIdx);
//            if(userInfo==null){
//
//            }

            commentRes = commentService.findAllByMessageId(messageBoardIdx, page);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS,commentRes);
    }
}
