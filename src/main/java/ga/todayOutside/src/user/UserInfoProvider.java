package ga.todayOutside.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.todayOutside.config.secret.Secret;
import ga.todayOutside.utils.AES128;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
        private final JwtService jwtService;

    @Autowired
    public UserInfoProvider(UserInfoRepository userInfoRepository, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
    }

    /**
     * 전체 회원 조회
     * @return List<UserInfoRes>
     * @throws BaseException
     */
    public List<GetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<UserInfo> userInfoList;
        try {
            if (word == null) { // 전체 조회
                userInfoList = userInfoRepository.findByStatus("ACTIVE");
            } else { // 검색 조회
                userInfoList = userInfoRepository.findByStatusAndNicknameIsContaining("ACTIVE", word);
            }
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            Long id = userInfo.getId();
            String email = userInfo.getEmail();
            return new GetUsersRes(id, email);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param snsId
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public GetUserRes retrieveUserInfo(Long snsId) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoByUserId(snsId);

        // 2. UserInfoRes로 변환하여 return
        Long id = userInfo.getId();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        return new GetUserRes(id, email, nickname);
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        // 1. DB에서 email로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoBySnsId(postLoginReq.getSnsId());

//        // 2. UserInfo에서 password 추출
//        String password;
//        try {
//            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getPassword());
//        } catch (Exception ignored) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
//        }
//
//        // 3. 비밀번호 일치 여부 확인
//        if (!postLoginReq.getPassword().equals(password)) {
//            throw new BaseException(BaseResponseStatus.WRONG_PASSWORD);
//        }

        // 3. Create JWT
        String jwt = jwtService.createJwt(userInfo.getId());

        // 4. PostLoginRes 변환하여 return
        Long id = userInfo.getId();
        return new PostLoginRes(id, jwt);
    }

    /**
     * 회원 조회
     * @param userId
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByUserId(Long userId) throws BaseException {
        // 1. DB에서 UserInfo 조회
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }
        System.out.println(userInfo.getStatus());
        // 2. 존재하는 회원인지 확인
        if (userInfo == null || !userInfo.getStatus().equals("ACTIVE")) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    /**
     * 회원 조회
     * @param snsId
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoBySnsId(Long snsId) throws BaseException {
        // 1. snsId 이용해서 UserInfo DB 접근
        UserInfo userInfo;

        try {
            userInfo = userInfoRepository.findById(snsId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }

        if (userInfo == null ) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        // 3. UserInfo를 return
        return userInfo;
    }


}
