package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DisasterProvider {
    public ArrayList<DisasterInfo> makeModel(JSONArray messages) {
        ArrayList<DisasterInfo> result = new ArrayList<>();

        for (Object o : messages) {

            JSONObject jo = (JSONObject) o;

            String locationName = (String) jo.get("location_name");
            String location[] = locationName.split(" ");
            String state = location[0];
            String city = location[1];
            String msg = (String) jo.get("msg");

            result.add(new DisasterInfo(state, city, msg));
        }

        return result;
    }
}
