package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterAlarmReq;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.user.UserInfoProvider;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisasterAlarmService {

    private final UserInfoProvider userInfoProvider;
    private final DisasterAlarmRepository disasterAlarmRepository;
    private final DisasterProvider disasterProvider;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public DisasterAlarmService(UserInfoProvider userInfoProvider, DisasterAlarmRepository disasterAlarmRepository, DisasterProvider disasterProvider, UserInfoRepository userInfoRepository) {
        this.userInfoProvider = userInfoProvider;
        this.disasterAlarmRepository = disasterAlarmRepository;
        this.disasterProvider = disasterProvider;
        this.userInfoRepository = userInfoRepository;
    }

    /**
     * 알람 등록
     * @param name
     * @param userId
     * @throws BaseException
     */
    public void postAlarm(List<String> name, Long userId) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userId);
        if (userInfo == null) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

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

    public void alarm(ArrayList<DisasterInfo> infos) {

        //유저 리스트 조회
        List<UserInfo> userInfos = userInfoRepository.findAll();
        // 각 리스트마다 시, 도, 재난 정보 확인해서 모두 일치하면 알람 송신
        for (UserInfo user : userInfos) {

        }

        for (DisasterInfo disasterInfo : infos) {

        }

    }

}
