package ga.todayOutside.src.user;

import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.secret.Secret;
import ga.todayOutside.utils.AES128;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoProvider = userInfoProvider;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UserInfo existsUserInfo = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsUserInfo = userInfoProvider.retrieveUserInfoBySnsId(postUserReq.getSnsId());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != BaseResponseStatus.NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsUserInfo != null) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String email = postUserReq.getEmail();
        String nickname = postUserReq.getNickname();
        String picture = postUserReq.getPicture();
        Role role = postUserReq.getRole();
        String status = "ACTIVE";
        Long snsId = postUserReq.getSnsId();

        try {
            //password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
        }
        UserInfo userInfo = new UserInfo(email, nickname, picture, role, status, snsId);

        // 3. 유저 정보 저장
        try {
            userInfo = userInfoRepository.save(userInfo);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
        }

        // 4. JWT 생성
        String jwt = jwtService.createJwt(userInfo.getId());

        // 5. UserInfoLoginRes로 변환하여 return
        Long id = userInfo.getId();
        return new PostUserRes(id, jwt);
    }

    /**
     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    public PatchUserRes updateUserInfo(@NonNull Long userId, PatchUserReq patchUserReq, UserInfo userInfo) throws BaseException {
        System.out.println(1234);
        try {
            String email = patchUserReq.getEmail().concat("_edited");
            String nickname = patchUserReq.getNickname().concat("_edited");
            String picture = patchUserReq.getPicture().concat("_edited");
            String status = "ACTIVE";
            Long id = patchUserReq.getId();
            Role role = patchUserReq.getRole();

            userInfo.setStatus(status);
            userInfo.setEmail(email);
            userInfo.setNickname(nickname);
            userInfo.setPicture(picture);
            userInfo.setRole(role);

            userInfoRepository.save(userInfo);

            return new PatchUserRes(email, nickname, picture, role);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_PATCH_USER);
        }
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @throws BaseException
     */
    public void deleteUserInfo(Long userId) throws BaseException {
        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userId);

        // 2-1. 해당 UserInfo를 완전히 삭제
        try {
            userInfoRepository.delete(userInfo);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_DELETE_USER);
        }

        // 2-2. 해당 UserInfo의 status를 INACTIVE로 설정

//        userInfo.setStatus("INACTIVE");
//        try {
//            userInfoRepository.save(userInfo);
//        } catch (Exception ignored) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_DELETE_USER);
//        }
    }

}

