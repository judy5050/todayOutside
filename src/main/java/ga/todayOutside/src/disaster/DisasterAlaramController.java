package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarmReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/disaster/alarm")
public class DisasterAlaramController {

    @Autowired
    DisasterAlarmService disasterAlarmService;

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

//    @Scheduled(cron = "*/10 * * * * *")
//    public void test() {
//        //System.out.println("스케줄러 실행 : " + new Date());
//    }

}
