package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterHomeInfoRes;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("")
public class DisasterController {
    private final DisasterService disasterService;
    private final DisasterAlarmService disasterAlarmService;

    @Autowired
    public DisasterController(DisasterService disasterService, DisasterAlarmService disasterAlarmService) {
        this.disasterService = disasterService;
        this.disasterAlarmService = disasterAlarmService;
    }

    /**
     * 월별 조회
     * @param month
     * @param city
     * @param state
     * @return
     */
    @GetMapping("/disaster/month/{userIdx}")
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
    @GetMapping("/disaster/day/{userIdx}")
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

    /**
     * 홈 화면 조회
     * @return
     */
    @GetMapping("/home/disaster")
    public BaseResponse<List<DisasterHomeInfoRes>> getHomeInfo() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String result = df.format(today);
        String month = result.split("-")[1];
        String day = result.split("-")[2];

        try {
            ArrayList<DisasterInfo> todayDisaster = disasterService.filterByDay(month, day);
            if (todayDisaster == null) return new BaseResponse<>(BaseResponseStatus.FAILED_TO_GET_DISASTER);

            List<DisasterHomeInfoRes> disasterHomeInfoRes = disasterService.getHomeInfo(todayDisaster);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_DISASTER, disasterHomeInfoRes);
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
