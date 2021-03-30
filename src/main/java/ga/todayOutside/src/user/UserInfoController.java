
package ga.todayOutside.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.messageBoard.MessageBoardService;
import ga.todayOutside.src.messageBoard.models.GetMessageBoardRecentlyRes;
import ga.todayOutside.src.messageBoard.models.GetMyMessageListRes;
import ga.todayOutside.src.weather.WeatherService;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
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
    private final AddressService addressService;
    private final WeatherService weatherService;
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
    public BaseResponse<GetUserRes> getUser(@PathVariable Long userId) throws BaseException {
        Long jwtId = jwtService.getUserId();
        if (jwtId != userId) return new BaseResponse<>(BaseResponseStatus.NOT_MATCH_USER);

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
            if (exception.getStatus().getCode() == 3011) {
                PostUserRes postUserRes = userInfoService.existUser(params.getSnsId());
                return new BaseResponse<>(BaseResponseStatus.DUPLICATED_USER, postUserRes);
            }
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
     * 카카오 로그인 API
     * [POST] /users/login/kakao
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     *
     */
    @PostMapping("/login/kakao")
    public BaseResponse<PostKakaoLoginRes> login(@RequestBody PostKakaoLoginReq postKakaoLoginReq) throws BaseException {

        PostKakaoLoginRes postKakaoLoginRes = null;
        //GetKakaoUserRes getKakaoUserRes;
        RestTemplate rt = new RestTemplate();//http요청을 간단하게 해줄 수 있는 클래
        System.out.println(postKakaoLoginReq.getAccessToken());


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + postKakaoLoginReq.getAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");



        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);


        ResponseEntity<String> response=null;
        try {
            response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );
        }catch (HttpClientErrorException exception){
            int statusCode=exception.getStatusCode().value();


            //잘못된 형식
            if(statusCode==400){
                return new BaseResponse<>(BaseResponseStatus.INVALID_KAKAO);
            }

            else if(statusCode==401){
                return new BaseResponse<>(BaseResponseStatus.INVALID_KAKAO);
            }




        }


        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile profile = null;
        String jwt = null;
        UserInfo userInfo=null;

        //Model과 다르게 되있으면 그리고 getter setter가 없으면 오류가 날 것이다.
        try {
            profile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
            try {
                userInfo = userInfoProvider.findOne(profile.getId());//snsId 값 넘김


            }catch (BaseException exception){
                return new BaseResponse<>(BaseResponseStatus.NEW_KAKAO_USERS,new PostKakaoLoginRes(null,null,profile.getKakao_account().getEmail(), profile.getId()) );
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();


        }


        // 변경전 jwt=jwtService.createJwt(userInfo.getId(), userInfo.getUserName(), userInfo.getSnsId());
        jwt=jwtService.createJwt(userInfo.getId());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_KAKAO_LOGIN, new PostKakaoLoginRes(userInfo.getId(),jwt,userInfo.getEmail(),userInfo.getSnsId()));
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
     * 자동 로그 API
     * [GET] /users/jwt
     * @return BaseResponse<Void>
     */
    @GetMapping("/jwt")
    public BaseResponse<PostLoginRes> jwt() {
        try {
            Long userId = jwtService.getUserId();
            UserInfo userInfo = userInfoService.findByUserIdx(userId);

            PostLoginRes postLoginRes = new PostLoginRes(
                    userId, userInfo.getEmail(),
                    userInfo.getSnsId());

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_JWT, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 게시글 조회 API
     * 주디
     */
    @ResponseBody
    @GetMapping("/messageList")
    public BaseResponse<List<GetMyMessageListRes>> getUserMessageList(@RequestParam("page")int page)
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



        return  new BaseResponse(BaseResponseStatus.SUCCESS_READ_MESSAGE_BOARD,messageAllByUserIdx);
    }

    /**
     * 알람 설정 on off
     */
    @PostMapping("/alarm")
    public BaseResponse<Void> alarm(@RequestParam boolean notice, @RequestParam boolean disaster) {

        UserInfo userInfo = null;

        try {
            Long userId = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userId);
            userInfoService.changeAlarm(userInfo, notice, disaster);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 홈 화면 모든 데이터 합치기
     */

    @GetMapping("/home")
    @ResponseBody
    public BaseResponse<ArrayList> home() throws java.text.ParseException {


        weatherService.date();
        Long userIdx;
        List<Address> address=null;
        ArrayList arrayList=new ArrayList();
        Map allAddressesByUserIdx=null;
        JSONArray allAddressesByUserIdx1;
        ArrayList allAddressesByUserIdx2=null;
        ArrayList allAddressesByUserIdx3=new ArrayList();

        try {

            //유저 정보 받아옴 토큰에서
            userIdx = jwtService.getUserId();

            //해당 유저 찾기 없을경우 오류값 반환
            UserInfo byUser = userInfoService.findByUserIdx(userIdx);
            System.out.println(byUser.getId());

            //유저에 해당하는 주소 가져옴
            address = addressService.findByAddressList(userIdx);
            allAddressesByUserIdx2 = addressService.getAllAddressesByUserIdx(userIdx);

            //날씨 게시글 쪽
            if(address.size()==0){
                throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);
            }
            else{

                for(int i=0;i<address.size();i++){
                    System.out.println("i = " + i);
                    address.get(i).getId();
                    System.out.println("address.get(i).getId() = " + address.get(i).getId());

                    arrayList.addAll( messageBoardService.getRecentlyTop1(address.get(i).getSecondAddressName()));
                    if(arrayList.size()-1!=i){
                        arrayList.add(new GetMessageBoardRecentlyRes("N"));

                    }

                }

            }


        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        allAddressesByUserIdx3.addAll(allAddressesByUserIdx2);
        allAddressesByUserIdx3.addAll(arrayList);
        allAddressesByUserIdx2.addAll(arrayList);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS_HOME_WEATHER, allAddressesByUserIdx3);
    }


}