package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DisasterProvider {

    @Autowired
    DisasterRepository disasterRepository;

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
            Long msgIdx = Long.parseLong( (String)jo.get("md101_sn") );

            /**
             *  저장 된 메세지면 return
             */


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

            result.add(new DisasterInfo(state, city, msg, createDate, msgIdx));
        }

        return result;
    }

    /**
     * ArrayList 형식을 JSONObject로 변환
     * @param map
     * @return
     */
    public JSONObject MapToJSON(Map<String, ArrayList<DisasterInfo>> map) {

        JSONObject resultMap = new JSONObject();

        for (String key : map.keySet()) {

            ArrayList<DisasterInfo> al = map.get(key);
            JSONArray ja = new JSONArray();

            for (DisasterInfo d : al)
                ja.add(d);

            resultMap.put(key, ja);

        }

        return resultMap;
    }

    /**
     * DB등록
     * @param disasterInfos
     * @return
     */
    public void postMsg(ArrayList<DisasterInfo> disasterInfos) {

        ArrayList<DisasterInfo> result = new ArrayList<>();
        DisasterInfoEntity resultInfo = null;

        for (DisasterInfo info : disasterInfos) {

            resultInfo = disasterRepository.findByMsgIdx(info.getMsgIdx()).orElse(null);
            //등록 되지 않은 데이터만 등록
            if (resultInfo != null) return;

            DisasterInfoEntity saveInfo = DisasterInfoEntity.builder()
                    .msg(info.getMsg()).msgIdx(info.getMsgIdx())
                    .state(info.getState()).city(info.getCity())
                    .createDate(info.getCreateDate())
                    .build();

            disasterRepository.save(saveInfo);

        }

        return;
    }

    /**
     * 필터 키워드
     * @param msg
     * @return
     */

    public String findKeyword(String msg) {
        String result = "";
        /* 필터 키워드
        질병, 지진, 태풍, 해일, 홍수, 호우, 강풍, 대설, 한파, 폭염, 건조, 황사

        미세먼지, 화재,
         */
        //질병
        Pattern pattern1 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*코로나+[\\w\\W\\d\\D\\s\\S]*");
        Pattern pattern2 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*방역+[\\w\\W\\d\\D\\s\\S]*");
        Pattern pattern3 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*확진자+[\\w\\W\\d\\D\\s\\S]*");
        Pattern pattern4 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*접촉자+[\\w\\W\\d\\D\\s\\S]*");
        Pattern pattern1_1 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*진료소+[\\w\\W\\d\\D\\s\\S]*");


        //지진
        Pattern pattern5 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*지진+[\\w\\W\\d\\D\\s\\S]*");
        //태풍
        Pattern pattern6 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*태풍+[\\w\\W\\d\\D\\s\\S]*");
        //해일
        Pattern pattern7 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*해일+[\\w\\W\\d\\D\\s\\S]*");
        //홍수
        Pattern pattern8 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*홍수+[\\w\\W\\d\\D\\s\\S]*");
        //호우
        Pattern pattern9 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*호우+[\\w\\W\\d\\D\\s\\S]*");
        //강풍
        Pattern pattern10 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*대설+[\\w\\W\\d\\D\\s\\S]*");
        //대설
        Pattern pattern11 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*코로나+[\\w\\W\\d\\D\\s\\S]*");
        //한파
        Pattern pattern12 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*한파+[\\w\\W\\d\\D\\s\\S]*");
        //폭염
        Pattern pattern13 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*폭염+[\\w\\W\\d\\D\\s\\S]*");
        //건조
        Pattern pattern14 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*건조+[\\w\\W\\d\\D\\s\\S]*");
        //황사
        Pattern pattern15 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*황사+[\\w\\W\\d\\D\\s\\S]*");
        //화재
        Pattern pattern16 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*화재+[\\w\\W\\d\\D\\s\\S]*");
        Pattern pattern16_1 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*산불+[\\w\\W\\d\\D\\s\\S]*");
        //미세먼지
        Pattern pattern17 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*미세먼지+[\\w\\W\\d\\D\\s\\S]*");

        Matcher matcher;
        if (pattern1.matcher(msg).matches() || pattern2.matcher(msg).matches() ||
                pattern3.matcher(msg).matches() || pattern4.matcher(msg).matches() ||
                pattern1_1.matcher(msg).matches()) {

            result = "코로나";

        } else if (pattern5.matcher(msg).matches()) {
            result = "지진";
        } else if (pattern6.matcher(msg).matches()) {
            result = "태풍";
        } else if (pattern7.matcher(msg).matches()) {
            result = "해일";
        } else if (pattern8.matcher(msg).matches()) {
            result = "홍수";
        } else if (pattern9.matcher(msg).matches()) {
            result = "호우";
        } else if (pattern10.matcher(msg).matches()) {
            result = "강풍";
        } else if (pattern11.matcher(msg).matches()) {
            result = "대설";
        } else if (pattern12.matcher(msg).matches()) {
            result = "한파";
        } else if (pattern13.matcher(msg).matches()) {
            result = "폭염";
        } else if (pattern14.matcher(msg).matches()) {
            result = "건조";
        } else if (pattern15.matcher(msg).matches()) {
            result = "황사";
        } else if (pattern16.matcher(msg).matches() || pattern16_1.matcher(msg).matches() ) {
            result = "화재";
        } else if (pattern17.matcher(msg).matches()) {
            result = "미세먼지";
        }

        return result;
    }
}
