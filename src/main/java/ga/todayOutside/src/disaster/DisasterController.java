package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarmReq;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/disaster")
public class DisasterController {
    private final DisasterService disasterService;

    @Autowired
    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    /**
     * 재난 정보 조회, DB 저장 -> 여기서 알람이랑 연동될 예정
     * -> 주기적으로 조회 알림 기능할 예
     */
    @GetMapping("/info")
    public Map<String, Object> getInfomation() throws ParseException {
        //재난페이지 조회

        Map<String, Object> result = disasterService.getImfomation();

        Integer status = (Integer) result.get("status");
        String jsonString = (String) result.get("body");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray disasterMsg = (JSONArray) jsonObj.get("DisasterMsg");
        JSONObject row = (JSONObject) disasterMsg.get(1);
        JSONArray messages = (JSONArray) row.get("row");

        //DB 등록
        ArrayList<DisasterInfo> newInfo = disasterService.postMsg(messages);


        return result;
    }

    /**
     * 월별 조회
     * @param month
     * @param city
     * @param state
     * @return
     */
    @GetMapping("/month/{userIdx}")
    public BaseResponse<Map<String, Object>> getMonth(@RequestParam String month, @RequestParam String city,
                                                      @RequestParam String state, @PathVariable Long userIdx) {

        int numberMonth = Integer.parseInt(month);

        if (numberMonth > 12 || numberMonth < 1) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MONTH_OR_DAY);
        }
        try {
            ArrayList<DisasterInfo> infos = disasterService.filterByMonth(month);
            Map<String, Object> result = disasterService.filter(infos, city, state, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_DISASTER, result);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 일별 조회
     * @param month
     * @param day
     * @param city
     * @param state
     * @return
     */
    @GetMapping("/day/{userIdx}")
    public BaseResponse<Map<String, Object>> getDay(@RequestParam String month, @RequestParam String day,
                                                    @RequestParam String city, @RequestParam String state, @PathVariable Long userIdx) {

        Map<String, Object> result = new HashMap<>();

        int numberMonth = Integer.parseInt(month);
        int numberDay = Integer.parseInt(day);

        if (numberMonth > 12 || numberMonth < 1 || numberDay > 31 || numberDay < 1) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MONTH_OR_DAY);
        }

        try {
            ArrayList<DisasterInfo> infos = disasterService.filterByDay(month, day);
            result = disasterService.filter(infos, city, state, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_DISASTER, result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
