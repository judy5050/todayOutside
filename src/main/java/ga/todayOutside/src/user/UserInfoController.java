
package ga.todayOutside.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.src.messageBoard.MessageBoardService;
import ga.todayOutside.src.messageBoard.models.GetMyMessageListRes;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserInfoController {

    //변경후 (judy)
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final KakaoService kakaoService;
    private final MessageBoardService messageBoardService;

    // 변경전
//    @Autowired
//    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService, JwtService jwtService, KakaoService kakaoService) {
//        this.userInfoProvider = userInfoProvider;
//        this.userInfoService = userInfoService;
//        this.jwtService = jwtService;
//        this.kakaoService = kakaoService;
//
//    }

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     * @return BaseResponse<List<GetUsersRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUserReq>> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUserReq> getUserReqList = userInfoProvider.retrieveUserInfoList(word);
            if (word == null) {
                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USERS, getUserReqList);
            } else {
                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_SEARCH_USERS, getUserReqList);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 조회 API
     * [GET] /users/:userId
     * @PathVariable userId
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserRes> getUser(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
        }

        try {
            GetUserRes getUserRes = userInfoProvider.retrieveUserInfo(userId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USER, getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq params) {

        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(params);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /jwt
     * @PathVariable userId
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/jwt")
    public BaseResponse<PatchUserRes> patchUsers(@RequestBody PatchUserReq parameters) throws BaseException {

        Long id = jwtService.getUserId();
        try {
            UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(id);
            PatchUserRes patchUserRes = userInfoService.updateUserInfo(id, parameters, userInfo);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_USER, patchUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 로그인 API
     * [POST] /users/login/kakao
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @PostMapping("/login/kakao")
    public BaseResponse<PostLoginRes> login(@RequestParam String accessToken) throws BaseException {

        //토큰 유저 정보 조회
        Map<String, Object> result = kakaoService.getUserInfo(accessToken);
        Map<String, Object> body =  (Map<String, Object>) result.get("body");
        Map<String, Object> kakaoAccount =  (Map<String, Object>) body.get("kakao_account");

        String userEmail = kakaoAccount.get("email").toString();
        String snsId = body.get("id").toString();

        PostLoginReq postLoginReq = new PostLoginReq(userEmail, Long.parseLong(snsId));
        // 2. Login

        try {
            PostLoginRes res = userInfoProvider.login(postLoginReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_LOGIN, res);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/jwt
     * @PathVariable userId
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/jwt")
    public BaseResponse<Void> deleteUsers() throws BaseException {
        Long userId = jwtService.getUserId();
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
        }

        try {
            userInfoService.deleteUserInfo(userId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * JWT 검증 API
     * [GET] /users/jwt
     * @return BaseResponse<Void>
     */
    @GetMapping("/jwt")
    public BaseResponse<Void> jwt() {
        try {
            Long userId = jwtService.getUserId();
            userInfoProvider.retrieveUserInfo(userId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 게시글 조회 API
     */
    @ResponseBody
    @GetMapping("/messageList")
    public BaseResponse<List<GetMyMessageListRes>> getUserMessageList(@RequestParam("page")String page)
    {
        Long userIdx;
        UserInfo userInfo;
        List<GetMyMessageListRes> messageAllByUserIdx;
        try {

            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
            messageAllByUserIdx = messageBoardService.findMessageAllByUserIdx(userIdx, page);


        }catch (BaseException exception){

            return new BaseResponse<>(exception.getStatus());
        }



        return  new BaseResponse(BaseResponseStatus.SUCCESS,messageAllByUserIdx);
    }
}