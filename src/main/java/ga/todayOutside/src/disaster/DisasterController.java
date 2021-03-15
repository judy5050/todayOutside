package ga.todayOutside.src.disaster;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 재난 정보 조회 api
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

        result = disasterService.filter(messages);

        return result;
    }
}
