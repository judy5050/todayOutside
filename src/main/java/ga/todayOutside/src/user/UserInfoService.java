package ga.todayOutside.src.user;

import ga.todayOutside.src.address.AddressRepository;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.PostAddressReq;
import ga.todayOutside.src.disaster.DisasterAlarmRepository;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;
    private final AddressRepository addressRepository;
    private final DisasterAlarmRepository disasterAlarmRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService, AddressRepository addressRepository, DisasterAlarmRepository disasterAlarmRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoProvider = userInfoProvider;
        this.jwtService = jwtService;
        this.addressRepository = addressRepository;
        this.disasterAlarmRepository = disasterAlarmRepository;
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
            existsUserInfo = userInfoProvider.retrieveUserInfoBySnsId(postUserReq.getSnsId());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != BaseResponseStatus.NOT_FOUND_USER) {
                throw exception;
            }
        }

        // 중복된 닉네임 조회 중복시 false 리턴
        boolean duplication = userInfoProvider.checkDuplication(postUserReq.getNickname());

        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (!duplication ) {
            throw new BaseException(BaseResponseStatus.DUPlICATED_NICKNAME);
        }

        if (existsUserInfo != null) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String email = postUserReq.getEmail();
        String nickname = postUserReq.getNickname();
        String picture = postUserReq.getPicture();
        Long snsId = postUserReq.getSnsId();
        List<PostAddressReq> postAddressReqs = postUserReq.getAddressInfos();
        String noticeAlarmStatus = "Y";
        String disasterAlarmStatus = "Y";
        Long heartNum = (long) 0;
        List<Long> addressIds = new ArrayList<>();
        String targetToken = postUserReq.getTargetToken();

        UserInfo userInfo = UserInfo.builder()
                .email(email).nickname(nickname)
                .picture(picture).snsId(snsId)
                .noticeAlarmStatus(noticeAlarmStatus).disasterAlarmStatus(disasterAlarmStatus)
                .heartNum(heartNum)
                .build();

        // 3. 유저 정보 저장
        try {
            userInfo = userInfoRepository.save(userInfo);

            //주소 저장 로직
            int orderCnt = 1;
            for (PostAddressReq postAddressReq : postAddressReqs) {

                Address address = Address.builder()
                        .userInfo(userInfo)
                        .firstAddressName(postAddressReq.getFirstAddressName())
                        .secondAddressName(postAddressReq.getSecondAddressName())
                        .addressOrder(orderCnt++)
                        .build();

                address = addressRepository.save(address);
                addressIds.add(address.getId());
            }

        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
        }

        //유저 알람 생성
        DisasterAlarm disasterAlarm = new DisasterAlarm();
        disasterAlarm.setUserIdx(userInfo.getId());
        disasterAlarm.setTargetToken(targetToken);
        disasterAlarmRepository.save(disasterAlarm);

        // 4. JWT 생성
        String jwt = jwtService.createJwt(userInfo.getId());

        // 5. UserInfoLoginRes로 변환하여 return
        Long id = userInfo.getId();
        return new PostUserRes(id, email, snsId, jwt, addressIds);
    }

    /**
     * 회원가입 -> 존재하는 유저일 경우 이용하는 서비스
     * @param snsId
     * @return
     */
    public PostUserRes existUser(Long snsId) {
        UserInfo userInfo = userInfoRepository.findBySnsId(snsId).orElse(null);
        Long userId = userInfo.getId();
        String jwt = "";
        List<Long> addressIds = addressRepository.findByUserIdxForAddressId(userId);

        PostUserRes postUserRes = new PostUserRes(userId, userInfo.getEmail(),
                userInfo.getSnsId(), jwt, addressIds);

        return postUserRes;
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

            boolean duplication = userInfoProvider.checkDuplication(nickname);
            if (!duplication) {
                throw new BaseException(BaseResponseStatus.DUPlICATED_NICKNAME);
            }

            if (!nickname.equals(""))
                userInfo.setNickname(nickname);
            userInfo.setEmail(email);
            userInfo.setPicture(picture);

            userInfoRepository.save(userInfo);

            return new PatchUserRes(userId, userInfo.getNickname(), picture, email);
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

    }

    public void changeAlarm(UserInfo userInfo, boolean notice, boolean disaster) throws BaseException {

        if (notice) {
            if (userInfo.getNoticeAlarmStatus().equals("Y")) {
                userInfo.setNoticeAlarmStatus("N");
            } else {
                userInfo.setNoticeAlarmStatus("Y");
            }
        }

        if (disaster) {
            if (userInfo.getDisasterAlarmStatus().equals("Y")) {
                userInfo.setDisasterAlarmStatus("N");
            } else {
                userInfo.setDisasterAlarmStatus("Y");
            }
        }
        userInfoRepository.save(userInfo);
    }

    //주디
    //유저 하트 수 증가
    @Transactional
    public void updateUserHeartPlus(UserInfo userInfo1) {
       userInfoRepository.updateUserHeartPlus(userInfo1.getId());

    }

    //주디
    //유저 하트 수 감소
    @Transactional
    public void updateUserHeartSub(UserInfo userInfo){
        userInfoRepository.updateUserHeartSub(userInfo.getId());
    }

    /**
     * 주디
     * 유저 이야기 수 증가
     */
    @Transactional
    public void updateUserTalk(UserInfo userInfo) {
        userInfoRepository.updateUserTalkPlus(userInfo.getId());
    }

    /**
     * 주디
     * 유저 이야기 수 감소
     */

    @Transactional
    public void updateUserTalkSub(UserInfo userInfo){
        userInfoRepository.updateUserTalkSub(userInfo.getId());
    }
}

