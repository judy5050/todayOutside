package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DisasterProvider {

    /**
     * 재난 정보 모델 만들기
     * @param messages
     * @return 재난 정보 리스트
     */
    public ArrayList<DisasterInfo> makeModel(JSONArray messages) {
        ArrayList<DisasterInfo> result = new ArrayList<>();

        for (Object o : messages) {

            JSONObject jo = (JSONObject) o;

            String locationName = (String) jo.get("location_name");
            String location[] = locationName.split(" ");
            String state = "";
            String city = "";
            String msg = (String) jo.get("msg");
            String createDate = (String) jo.get("create_date");

            /**
             * 세종시는 특별자치시 - 도, 시가 나뉘지 않는다.
             */
            if (location.length == 1) {
                state = location[0];
                city = "세종시";
            }
            else {
                state = location[0];
                city = location[1];
            }

            result.add(new DisasterInfo(state, city, msg, createDate));
        }

        return result;
    }
}
