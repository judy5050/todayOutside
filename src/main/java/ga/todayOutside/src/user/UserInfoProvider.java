package ga.todayOutside.src.user;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.src.address.AddressRepository;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.GetAddressRes;
import ga.todayOutside.src.address.model.PostAddressRes;
import ga.todayOutside.utils.JwtService;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;
    private final AddressRepository addressRepository;

    @Autowired
    public UserInfoProvider(UserInfoRepository userInfoRepository, JwtService jwtService, AddressRepository addressRepository) {
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
        this.addressRepository = addressRepository;
    }

    /**
     * 전체 회원 조회
     * @return List<UserInfoRes>
     * @throws BaseException
     */
    public List<GetUserReq> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<UserInfo> userInfoList = null;
        try {
            if (word == null) { // 전체 조회
                //userInfoList = userInfoRepository.findByStatus("ACTIVE");
            } else { // 검색 조회
                //userInfoList = userInfoRepository.findByStatusAndNicknameIsContaining("ACTIVE", word);
            }
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            Long id = userInfo.getId();
            String email = userInfo.getEmail();
            return new GetUserReq(id, email);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userId
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public GetUserRes retrieveUserInfo(Long userId) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoByUserId(userId);

        // 2. UserInfoRes로 변환하여 return
        Long id = userInfo.getId();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String noticeAlarmStatus = userInfo.getNoticeAlarmStatus();
        String heartNum = userInfo.getHeartNum().toString();
        Long talkNum = userInfo.getTalkNum();
        String profile = userInfo.getPicture();

        List<GetAddressRes> address = addressRepository.findAllByUserIdx(id);

        return new GetUserRes(id, email, nickname, noticeAlarmStatus, heartNum, talkNum, profile, address);
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

        // 3. Create JWT
        String jwt = jwtService.createJwt(userInfo.getId());

        // 4. PostLoginRes 변환하여 return
        Long id = userInfo.getId();
        return new PostLoginRes(id, userInfo.getEmail(),
                userInfo.getSnsId());
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

        // 2. 존재하는 회원인지 확인
        if (userInfo == null || !userInfo.getIsDeleted().equals("N")) {
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
            userInfo = userInfoRepository.findBySnsId(snsId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_USER);
        }

        //존재하는 회원인지 확인
        if (userInfo == null) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        // 3. UserInfo를 return
        return userInfo;
    }

    /**
     * 닉네임 중복 조회
     * 중복일 경우 false 리턴
     * @param name
     * @return
     */
    public boolean checkDuplication(String name) {
        UserInfo userInfo = userInfoRepository.findByNickname(name).orElse(null);
        if (userInfo == null || userInfo.getIsDeleted().equals("Y")) return true;
        return false;
    }

    //카카오 snsId 값
    public UserInfo findOne(Long snsId)throws BaseException{

        List<UserInfo> result=null;
        result=userInfoRepository.findByUser(snsId,"N");
        if(result!=null&&result.size()>0){
            return result.get(0);
        }
        else{
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

    }


}