package ga.todayOutside.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final KakaoService kakaoService;

    @Autowired
    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService, JwtService jwtService, KakaoService kakaoService) {
        this.userInfoProvider = userInfoProvider;
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.kakaoService = kakaoService;
    }

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     * @return BaseResponse<List<GetUsersRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUsersRes> getUsersResList = userInfoProvider.retrieveUserInfoList(word);
            if (word == null) {
                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USERS, getUsersResList);
            } else {
                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_SEARCH_USERS, getUsersResList);
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
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq params) throws JsonProcessingException {
        // 토큰 검증
//        String snsId = "";
//        Map<String, Object> result = kakaoService.accessToken(accessToken);
//        System.out.println(result.get("body"));
////
////        // object의 snsid 값 추출 과정
////        String [] s1 = result.get("body").toString().split(",");
////        String [] s2 = s1[0].split("=");
////        snsId = s1[1];
//
//        if (!result.get("status").equals("200")) {
//            //추후 수정 해야함
//            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
//        }


//        // 1. Body Parameter Validation
//        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
//            return new BaseResponse<>(BaseResponseStatus.EMPTY_EMAIL);
//        }
//        if (!ValidationRegex.isRegexEmail(parameters.getEmail())){
//            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
//        }
//        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
//            return new BaseResponse<>(BaseResponseStatus.EMPTY_PASSWORD);
//        }
//        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
//            return new BaseResponse<>(BaseResponseStatus.EMPTY_NICKNAME);
//        }
//
        // 2. Post UserInfo

        System.out.println(params);
        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(params);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /users/:userId
     * @PathVariable userId
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<PatchUserRes> patchUsers(@PathVariable Integer userId, @RequestBody PatchUserReq parameters) {

        if (userId == null || userId <= 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
        }

//        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
//            return new BaseResponse<>(BaseResponseStatus.DO_NOT_MATCH_PASSWORD);
//        }
        System.out.println(userId);
        try {

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_USER, userInfoService.updateUserInfo(userId, parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }




    /**
     * 로그인 API
     * [POST] /users/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @PostMapping("/login")
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
     * [DELETE] /users/:userId
     * @PathVariable userId
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{userId}")
    public BaseResponse<Void> deleteUsers(@PathVariable Long userId) {
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
}