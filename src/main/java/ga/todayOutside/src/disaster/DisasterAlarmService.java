package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterAlarmReq;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.user.UserInfoProvider;
import ga.todayOutside.src.user.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisasterAlarmService {

    @Autowired
    UserInfoProvider userInfoProvider;
    @Autowired
    DisasterAlarmRepository disasterAlarmRepository;
    @Autowired
    DisasterProvider disasterProvider;

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



    }

}
