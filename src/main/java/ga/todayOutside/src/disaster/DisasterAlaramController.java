package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.FCM.FirebaseCloudMessageService;
import ga.todayOutside.src.disaster.model.DisasterAlarmReq;
import ga.todayOutside.src.disaster.model.DisasterAlarmUser;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/disaster/alarm")
public class DisasterAlaramController {

    private final DisasterAlarmService disasterAlarmService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final DisasterProvider disasterProvider;
    private final DisasterService disasterService;

    @Autowired
    public DisasterAlaramController(DisasterAlarmService disasterAlarmService, FirebaseCloudMessageService firebaseCloudMessageService, DisasterProvider disasterProvider, DisasterService disasterService) {
        this.disasterAlarmService = disasterAlarmService;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.disasterProvider = disasterProvider;
        this.disasterService = disasterService;
    }

    @GetMapping("/{userIdx}")
    public Map<String, Object> getAlramInfo() {


        return new HashMap<>();
    }

    /**
     * 알람등록
     */
    @PostMapping("")
    public BaseResponse<Void> postAlarm(@RequestBody DisasterAlarmReq disasterAlarmReq) {

        try {
            disasterAlarmService.postAlarm(disasterAlarmReq.getName(), disasterAlarmReq.getUserIdx());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_DISASTER_ALARM);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 알람 스케줄러 5분마다 알림 조회 및 전송
     */
//    @Scheduled(cron = "* */5 * * * *")
//    public void alarm() {
//        System.out.println("재난 문자 조회 및 알람 실행 : " + new Date());
//        List<DisasterAlarmUser> disasterAlarmUserList = disasterAlarmService.alarm();
//    }


//    /**
//     * 재난 정보 조회, DB 저장 -> 여기서 알람이랑 연동될 예정
//     * -> 주기적으로 조회 알림 기능할 예
//     */
//    @Scheduled(cron = "* */5 * * * *")
//    public void getInfomation() throws ParseException, IOException {
//        //재난페이지 조회
//
//        Map<String, Object> result = disasterService.getImfomation();
//        if (result == null) return;
//        JSONArray messages = disasterProvider.MapToMessage(result);
//        //DB 등록
//        ArrayList<DisasterInfo> newInfo = disasterService.postMsg(messages);
//        List<DisasterAlarmUser> disasterAlarmUsers = disasterAlarmService.alarm(newInfo);
//        disasterAlarmService.sendMessage(disasterAlarmUsers);
//
//        return;
//    }
}
