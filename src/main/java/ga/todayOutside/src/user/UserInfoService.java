package ga.todayOutside.src.user;

import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.secret.Secret;
import ga.todayOutside.utils.AES128;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

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
         *userInfo 찾기
         * judy 5050 작성
         */
        public UserInfo findByUserIdx(Long userIdx) throws BaseException {

            UserInfo userInfo;
            userInfo= userInfoRepository.findById(userIdx).orElse(null);
            if(userInfo==null){

                throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);

            }
            return userInfo;

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
            System.out.println("회원 조회");
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
        Long snsId = postUserReq.getSnsId();
        String mainLocation = postUserReq.getMainLocation();
        String subLocation = postUserReq.getSubLocation();
        String noticeAlarmStatus = "Y";
        String disasterAlarmStatus = "Y";
        Long heartNum = (long) 0;
        String isDeleted = "N";

        try {
            //password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
        }
        //수정전
//        UserInfo userInfo = UserInfo.builder()
//                .email(email).nickname(nickname)
//                .userMainLocation(mainLocation).userSubLocation(subLocation)
//                .picture(picture).snsId(snsId)
//                .noticeAlarmStatus(noticeAlarmStatus).disasterAlarmStatus(disasterAlarmStatus)
//                .heartNum(heartNum).isDeleted(isDeleted)
//                .build();

        //수정후
        UserInfo userInfo = UserInfo.builder()
                .email(email).nickname(nickname)
                .picture(picture).snsId(snsId)
                .noticeAlarmStatus(noticeAlarmStatus).disasterAlarmStatus(disasterAlarmStatus)
                .heartNum(heartNum).isDeleted(isDeleted)
                .build();

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

        try {
            String email = patchUserReq.getEmail();
            String nickname = patchUserReq.getNickname();
            String picture = patchUserReq.getPicture();
            String noticeAlarmStatus = patchUserReq.getNoticeAlarmStatus();
            String disasterAlarmStatus = patchUserReq.getDisasterAlarmStatus();
            String userMainLocation = patchUserReq.getUserMainLocation();
            String userSubLocation = patchUserReq.getUserSubLocation();

            //수정전
//            userInfo.setEmail(email);
//            userInfo.setNickname(nickname);
//            userInfo.setPicture(picture);
//            userInfo.setNoticeAlarmStatus(noticeAlarmStatus);
//            userInfo.setDisasterAlarmStatus(disasterAlarmStatus);
//            userInfo.setUserMainLocation(userMainLocation);
//            userInfo.setUserSubLocation(userSubLocation);

            //수정후
            userInfo.setEmail(email);
            userInfo.setNickname(nickname);
            userInfo.setPicture(picture);
            userInfo.setNoticeAlarmStatus(noticeAlarmStatus);
            userInfo.setDisasterAlarmStatus(disasterAlarmStatus);


            String jwt = jwtService.createJwt(userInfo.getId());
            userInfoRepository.save(userInfo);

            return new PatchUserRes(userId, jwt);
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

