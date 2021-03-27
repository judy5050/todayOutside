package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterFilterRes;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public JSONObject MapToJSON(Map<String, ArrayList<DisasterFilterRes>> map) {

        JSONObject resultMap = new JSONObject();
        int total = 0;

        //각 재난 정보 가져오기
        for (String key : map.keySet()) {

            ArrayList<DisasterFilterRes> al = map.get(key);
            JSONArray ja = new JSONArray();
            int cnt = 0;

            //각 재난별 정보 가져오기
            for (DisasterFilterRes d : al){
                ja.add(d);
                total += 1;
                cnt += 1;
            }

            resultMap.put(key, ja);

        }
        resultMap.put("total", total);
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
            if (resultInfo != null) continue;

            DisasterInfoEntity saveInfo = DisasterInfoEntity.builder()
                    .msg(info.getMsg()).msgIdx(info.getMsgIdx())
                    .state(info.getState()).city(info.getCity())
                    .createDate(info.getCreateDate())
                    .build();

            disasterRepository.save(saveInfo);

        }

        return;
    }

    public Set<String> alarmSeting() {

        Set<String> result = null;

        return result;
    }

    /**
     * 필터 키워드
     * @param msg
     * @return
     */

    public String findKeyword(String msg) {
        String result = "";
/* 필터 키워드 - 넘버링

        감염병 - 1, 지진 - 2, 태풍 - 3, 해일 - 4, 홍수 - 5, 호우 - 6, 강풍 - 7, 대설- 8, 한파 -9, 폭염 - 10, 건조 - 11, 황사 - 12
        미세먼지 13, 화재 14, 민방공 15, 수질 16  테러 17, 방사능 18, 위험물 19

         */
        //감염병
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
        //민방공
        Pattern pattern18 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*민방공+[\\w\\W\\d\\D\\s\\S]*");
        //수질
        Pattern pattern19 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*수질+[\\w\\W\\d\\D\\s\\S]*");
        //테러
        Pattern pattern20 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*테러+[\\w\\W\\d\\D\\s\\S]*");
        //방사능
        Pattern pattern21 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*테러+[\\w\\W\\d\\D\\s\\S]*");
        //위험물
        Pattern pattern22 = Pattern.compile("[\\w\\W\\d\\D\\s\\S]*위험물+[\\w\\W\\d\\D\\s\\S]*");


        Matcher matcher;
        if (pattern1.matcher(msg).matches() || pattern2.matcher(msg).matches() ||
                pattern3.matcher(msg).matches() || pattern4.matcher(msg).matches() ||
                pattern1_1.matcher(msg).matches()) {

            result = "감염병";

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
        } else if (pattern18.matcher(msg).matches()) {
            result = "민방공";
        } else if (pattern19.matcher(msg).matches()) {
            result = "수질";
        } else if (pattern20.matcher(msg).matches()) {
            result = "테러";
        } else if (pattern21.matcher(msg).matches()) {
            result = "방사능";
        }

        return result;
    }

    public DisasterAlarm makeDisasterAlarm(List<String> names, DisasterAlarm disasterAlarm) {
/* 필터 키워드 - 넘버링

        감염병 - 1, 지진 - 2, 태풍 - 3, 해일 - 4, 홍수 - 5, 호우 - 6, 강풍 - 7, 대설- 8, 한파 -9, 폭염 - 10, 건조 - 11, 황사 - 12
        미세먼지 13, 화재 14, 민방공 15, 수질 16  테러 17, 방사능 18, 위험물 19

         */
        if (disasterAlarm == null) {
            disasterAlarm = new DisasterAlarm();
        }

        disasterAlarm.setDisaster_1("N");
        disasterAlarm.setDisaster_2("N");
        disasterAlarm.setDisaster_3("N");
        disasterAlarm.setDisaster_4("N");
        disasterAlarm.setDisaster_5("N");
        disasterAlarm.setDisaster_6("N");
        disasterAlarm.setDisaster_7("N");
        disasterAlarm.setDisaster_8("N");
        disasterAlarm.setDisaster_9("N");
        disasterAlarm.setDisaster_10("N");
        disasterAlarm.setDisaster_11("N");
        disasterAlarm.setDisaster_12("N");
        disasterAlarm.setDisaster_13("N");
        disasterAlarm.setDisaster_14("N");
        disasterAlarm.setDisaster_15("N");
        disasterAlarm.setDisaster_16("N");
        disasterAlarm.setDisaster_17("N");
        disasterAlarm.setDisaster_18("N");
        disasterAlarm.setDisaster_19("N");

        for (String name : names) {

            if (name.equals("감염병"))  disasterAlarm.setDisaster_1("Y");
            else if (name.equals("지진"))  disasterAlarm.setDisaster_2("Y");
            else if (name.equals("태풍"))  disasterAlarm.setDisaster_3("Y");
            else if (name.equals("해일"))  disasterAlarm.setDisaster_4("Y");
            else if (name.equals("홍수"))  disasterAlarm.setDisaster_5("Y");
            else if (name.equals("호우"))  disasterAlarm.setDisaster_6("Y");
            else if (name.equals("강풍"))  disasterAlarm.setDisaster_7("Y");
            else if (name.equals("대설"))  disasterAlarm.setDisaster_8("Y");
            else if (name.equals("한파"))  disasterAlarm.setDisaster_9("Y");
            else if (name.equals("폭염"))  disasterAlarm.setDisaster_10("Y");
            else if (name.equals("건조"))  disasterAlarm.setDisaster_11("Y");
            else if (name.equals("황사"))  disasterAlarm.setDisaster_12("Y");
            else if (name.equals("미세먼지"))  disasterAlarm.setDisaster_13("Y");
            else if (name.equals("화재"))  disasterAlarm.setDisaster_14("Y");
            else if (name.equals("민방공"))  disasterAlarm.setDisaster_15("Y");
            else if (name.equals("수질"))  disasterAlarm.setDisaster_16("Y");
            else if (name.equals("테러"))  disasterAlarm.setDisaster_17("Y");
            else if (name.equals("방사능"))  disasterAlarm.setDisaster_18("Y");
            else if (name.equals("위험물"))  disasterAlarm.setDisaster_19("Y");

        }

        return disasterAlarm;
    }

    public Set<String> filterDisaster(DisasterAlarm disasterAlarm) {
        /* 필터 키워드 - 넘버링

        감염병 - 1, 지진 - 2, 태풍 - 3, 해일 - 4, 홍수 - 5, 호우 - 6, 강풍 - 7, 대설- 8, 한파 -9, 폭염 - 10, 건조 - 11, 황사 - 12
        미세먼지 13, 화재 14, 민방공 15, 수질 16  테러 17, 방사능 18, 위험물 19

         */
        Set<String> result = new HashSet<>();

        if (disasterAlarm.getDisaster_1().equals("Y")) result.add("감염병");
        if (disasterAlarm.getDisaster_2().equals("Y")) result.add("지진");
        if (disasterAlarm.getDisaster_3().equals("Y")) result.add("태풍");
        if (disasterAlarm.getDisaster_4().equals("Y")) result.add("해일");
        if (disasterAlarm.getDisaster_5().equals("Y")) result.add("홍수");
        if (disasterAlarm.getDisaster_6().equals("Y")) result.add("호우");
        if (disasterAlarm.getDisaster_7().equals("Y")) result.add("강풍");
        if (disasterAlarm.getDisaster_8().equals("Y")) result.add("대설");
        if (disasterAlarm.getDisaster_9().equals("Y")) result.add("한파");
        if (disasterAlarm.getDisaster_10().equals("Y")) result.add("폭염");
        if (disasterAlarm.getDisaster_11().equals("Y")) result.add("건조");
        if (disasterAlarm.getDisaster_12().equals("Y")) result.add("황사");
        if (disasterAlarm.getDisaster_13().equals("Y")) result.add("미세먼지");
        if (disasterAlarm.getDisaster_14().equals("Y")) result.add("화재");
        if (disasterAlarm.getDisaster_15().equals("Y")) result.add("민방공");
        if (disasterAlarm.getDisaster_16().equals("Y")) result.add("수질");
        if (disasterAlarm.getDisaster_17().equals("Y")) result.add("테러");
        if (disasterAlarm.getDisaster_18().equals("Y")) result.add("방사능");
        if (disasterAlarm.getDisaster_19().equals("Y")) result.add("위험물");

        return result;
    }
}
