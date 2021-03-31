package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.FCM.FirebaseCloudMessageService;
import ga.todayOutside.src.address.AddressRepository;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterAlarmUser;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.user.UserInfoProvider;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DisasterAlarmService {

    private final UserInfoProvider userInfoProvider;
    private final DisasterAlarmRepository disasterAlarmRepository;
    private final DisasterProvider disasterProvider;
    private final UserInfoRepository userInfoRepository;
    private final AddressRepository addressRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Autowired
    public DisasterAlarmService(UserInfoProvider userInfoProvider, DisasterAlarmRepository disasterAlarmRepository, DisasterProvider disasterProvider, UserInfoRepository userInfoRepository, AddressRepository addressRepository, FirebaseCloudMessageService firebaseCloudMessageService) {
        this.userInfoProvider = userInfoProvider;
        this.disasterAlarmRepository = disasterAlarmRepository;
        this.disasterProvider = disasterProvider;
        this.userInfoRepository = userInfoRepository;
        this.addressRepository = addressRepository;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }

    /**
     * 알람 등록
     * @param name
     * @param userId
     * @throws BaseException
     */
    public void postAlarm(List<String> name, Long userId) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userId);

        //등록된 알람이 있는지 확인
        DisasterAlarm disasterAlarm = disasterAlarmRepository.findByUserIdx(userId).orElse(null);

        try {
            disasterAlarm = disasterProvider.makeDisasterAlarm(name, disasterAlarm);
            disasterAlarm.setUserIdx(userId);

            disasterAlarmRepository.save(disasterAlarm);

        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_ALARAM);

        }
    }

    /**
     * 알람 송신할 유저데이터 만들기
     * @param infos
     * @return
     */
    public List<DisasterAlarmUser> alarm(ArrayList<DisasterInfo> infos) {

        List<DisasterAlarmUser> alarmUsers = new ArrayList<>();
        //유저 리스트 조회
        List<UserInfo> userInfos = userInfoRepository.findAll();

        // 각 리스트마다 시, 도, 재난 정보 확인해서 모두 일치하면 알람 송신
        for (UserInfo user : userInfos) {
            //삭제된 유저 제외
            if (user.getIsDeleted().equals("Y")) continue;

            Long userId = user.getId();
            //각 유저별로 재난 알람 조회
            DisasterAlarm disasterAlarm = disasterAlarmRepository.findByUserIdx(userId).orElse(null);
            Address address = addressRepository.findByUserIdxForFirstOrder(userId);
            if (address == null) continue;
            if (disasterAlarm == null) continue;

            String state = address.getFirstAddressName();
            String city = address.getSecondAddressName();
            Set<String> kinds = disasterProvider.filterDisaster(disasterAlarm);
            String targetToken = disasterAlarm.getTargetToken();
            List<String> alarmKinds = new ArrayList<>();

            if (targetToken.length() < 0) continue;
            for (DisasterInfo disasterInfo : infos) {
                String currentCity = disasterInfo.getCity();
                String currentState = disasterInfo.getState();
                String currentkind = disasterInfo.getKind();

                //등록한 정보가 전부 일치할 경우 알림 추가
                if (currentCity.equals(city) && currentState.equals(state) && kinds.contains(currentkind)) {
                    alarmKinds.add(currentkind);
                }
            }

            // 보낼게 없다면 다음 유저
            if (alarmKinds.size() < 1) continue;

            alarmUsers.add(new DisasterAlarmUser(alarmKinds, targetToken, address.getSecondAddressName()));
        }

        return alarmUsers;
    }

    /**
     * 유저들에게 알람 송신
     * @param disasterAlarmUsers
     * @throws IOException
     */
    public void sendMessage(List<DisasterAlarmUser> disasterAlarmUsers) throws IOException {

        for (DisasterAlarmUser user : disasterAlarmUsers) {

            String token = user.getTargetToken();

            for (String kind : user.getAlarmKinds()) {
                String title = "재난";
                String message = user.getSecondAddress()+ "에서 " + kind + "에 대한 재난문자가 왔어요!";
                firebaseCloudMessageService.sendMessageTo(token, title, message);
            }

        }

    }

}
