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
        Map<Object, ArrayList<Map<String, JSONObject>>> fileter = new HashMap<>();

        Integer status = (Integer) result.get("status");
        String jsonString = (String) result.get("body");

        // string json 형식으로 들어옴

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray disasterMsg = (JSONArray) jsonObj.get("DisasterMsg");
        JSONObject row = (JSONObject) disasterMsg.get(1);
        JSONArray messages = (JSONArray) row.get("row");

        for (Object o : messages) {
            Map<String, JSONObject> msgs = new HashMap<>();

            JSONObject jo = (JSONObject) o;
            String locationName = (String) jo.get("location_name");
            String location[] = locationName.split(" ");
            String state = location[0];
            String city = location[1];

            ArrayList<Map<String, JSONObject>> regCity = fileter.getOrDefault("state", new ArrayList<Map<String, JSONObject>>());
            regCity.add(msgs.put(city, jo));
            fileter.put(state, regCity);

        }

        /**
         출력 결과 {경기도=[null]}
         문제점 null 값으로 value가 입력 된다.
         지역에 다른 필터 메소드를 따로 만드는 것이 좋을 것 같음

         */
        System.out.println(fileter);
        return result;
    }
}
