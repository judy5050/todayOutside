package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/disaster")
public class DisasterController {
    private final DisasterService disasterService;

    @Autowired
    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    /**
     * 재난 정보 조회, DB 저장
     */
    @ResponseBody
    @GetMapping("/info")
    public Map<String, Object> getInfomation() throws ParseException {
        Map<String, Object> result = disasterService.getImfomation();

        Integer status = (Integer) result.get("status");
        String jsonString = (String) result.get("body");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray disasterMsg = (JSONArray) jsonObj.get("DisasterMsg");
        JSONObject row = (JSONObject) disasterMsg.get(1);
        JSONArray messages = (JSONArray) row.get("row");

        disasterService.postMsg(messages);

        return result;
    }

    @ResponseBody
    @GetMapping("/month")
    public BaseResponse<Map<String, Object>> getMonth(@RequestParam String month) {

        Map<String, Object> result = new HashMap<>();

        try {
            ArrayList<DisasterInfo> infos = disasterService.filterByMonth(month);
            result = disasterService.filter(infos);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_DISASTER, result);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @GetMapping("/day")
    public BaseResponse<Map<String, Object>> getDay(@RequestParam String month, @RequestParam String day) {

        Map<String, Object> result = new HashMap<>();

        try {
            ArrayList<DisasterInfo> infos = disasterService.filterByDay(month, day);
            result = disasterService.filter(infos);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_GET_DISASTER, result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
