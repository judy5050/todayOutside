package ga.todayOutside.src.weather;


import ga.todayOutside.config.secret.Secret;
import ga.todayOutside.src.user.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {

    static Map<String, Map> todayWeahterResult=new LinkedHashMap<>();

    static Map<String,String> clock_00=new HashMap<>();
    static Map<String,String> clock_01=new HashMap<>();
    static Map<String,String>clock_02=new HashMap<>();
    static Map<String,String> clock_03=new HashMap<>();
    static Map<String,String> clock_04=new HashMap<>();
    static Map<String,String> clock_05=new HashMap<>();
    static Map<String,String> clock_06=new HashMap<>();
    static Map<String,String> clock_07=new HashMap<>();
    static Map<String,String> clock_08=new HashMap<>();

    static Map<String,String> clock_09=new HashMap<>();
    static Map<String,String>clock_10=new HashMap<>();
    static Map<String,String> clock_11=new HashMap<>();
    static Map<String,String> clock_12=new HashMap<>();
    static Map<String,String> clock_13=new HashMap<>();
    static Map<String,String> clock_14=new HashMap<>();
    static Map<String,String> clock_15=new HashMap<>();
    static Map<String,String> clock_16=new HashMap<>();
    static Map<String,String> clock_17=new ManagedMap<>();

    static Map<String,String> clock_18=new HashMap<>();
    static Map<String,String>clock_19=new HashMap<>();
    static Map<String,String> clock_20=new HashMap<>();
    static Map<String,String> clock_21=new HashMap<>();
    static Map<String,String> clock_22=new HashMap<>();
    static Map<String,String> clock_23=new HashMap<>();

    //일주일 단위의 날씨 정보 조회 변수

    static Map<String,String> day1_high=new HashMap<>();
    static Map<String,String> day1_low=new HashMap<>();

    static Map<String,String> day2_high=new HashMap<>();
    static Map<String,String> day2_low=new HashMap<>();

    static Map<String,String> day3_high=new HashMap<>();
    static Map<String,String> day3_low=new HashMap<>();

    static Map<String,String> day4_high=new HashMap<>();
    static Map<String,String> day4_low=new HashMap<>();

    static Map<String,String> day5_high=new HashMap<>();
    static Map<String,String> day5_low=new HashMap<>();

    static Map<String,String> day6_high=new HashMap<>();
    static Map<String,String> day6_low=new HashMap<>();

    static Map<String,String> day7_high=new HashMap<>();
    static Map<String,String> day7_low=new HashMap<>();

    //최종 오늘의 날씨 return 변수
    static Map<String, Map> todayWeatherPer03Result=new LinkedHashMap<>();


    //3시간별 단위 날씨 조회
    //3시간을 1시간 단위로 쪼갬

    static Map<String,String> per_00=new HashMap<>();
    static Map<String,String> per_01=new HashMap<>();
    static Map<String,String> per_02=new HashMap<>();
    static Map<String,String> per_03=new HashMap<>();
    static Map<String,String> per_04=new HashMap<>();
    static Map<String,String> per_05=new HashMap<>();
    static Map<String,String> per_06=new HashMap<>();
    static Map<String,String> per_07=new HashMap<>();
    static Map<String,String> per_08=new HashMap<>();
    static Map<String,String> per_09=new HashMap<>();
    static Map<String,String> per_10=new HashMap<>();
    static Map<String,String> per_11=new HashMap<>();
    static Map<String,String> per_12=new HashMap<>();

    static Map<String,String> per_13=new HashMap<>();
    static Map<String,String> per_14=new HashMap<>();
    static Map<String,String> per_15=new HashMap<>();
    static Map<String,String> per_16=new HashMap<>();
    static Map<String,String> per_17=new HashMap<>();
    static Map<String,String> per_18=new HashMap<>();
    static Map<String,String> per_19=new HashMap<>();
    static Map<String,String> per_20=new HashMap<>();
    static Map<String,String> per_21=new HashMap<>();
    static Map<String,String> per_22=new HashMap<>();
    static Map<String,String> per_23=new HashMap<>();


    //당일~7일 데이터 정보 넘김
    static Map<String,String> day_01_high=new HashMap<>();
    static Map<String,String> day_01_low=new HashMap<>();
    static Map<String,String> day_02_high=new HashMap<>();
    static Map<String,String> day_02_low=new HashMap<>();
    static Map<String,String> day_03_high=new HashMap<>();
    static Map<String,String> day_03_low=new HashMap<>();
    static Map<String,String> day_04_high=new HashMap<>();
    static Map<String,String> day_04_low=new HashMap<>();
    static Map<String,String> day_05_high=new HashMap<>();
    static Map<String,String> day_05_low=new HashMap<>();
    static Map<String,String> day_06_high=new HashMap<>();
    static Map<String,String> day_06_low=new HashMap<>();
    static Map<String,String> day_07_high=new HashMap<>();
    static Map<String,String> day_07_low=new HashMap<>();

    static Map<String,String> weeklyResult=new LinkedHashMap<>();// 키값 자동정



    //나중에 유저에서 시 군 구.. 주소 값을 가져오기 위해 의존관계 주입
    private final UserInfoRepository userInfoRepository;

    public Map GetTodayWeather() throws IOException, ParseException {


        //시간을 받아오는 코드
        //조회하는 시간에서 +1 정보만 가져온다.
        Integer currentTime = LocalDateTime.now().getHour();
        Integer currentYear = LocalDate.now().getYear();
        Integer currentMonth = LocalDate.now().getMonthValue();
        Integer currentDate = LocalDate.now().getDayOfMonth();
        System.out.println("currentDate = " + currentDate);
        Integer min = LocalDateTime.now().getMinute();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = "60";    //위도
        String ny = "125";    //경도
        String baseTime = null;

        //분에 따른 baseTime 조절
        if (min <= 30) {
            Integer calTime;
            if (currentTime == 0) {
                calTime = 23;
            } else {
                calTime = currentTime - 1;
            }
            if (calTime < 10) {
                Integer.toString(calTime);
                String before30Minute = "0" + calTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간
                System.out.println("##########");
            }

        } else {
            if (currentTime < 10) {
                Integer.toString(currentTime);
                String before30Minute = "0" + currentTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간

            }
            String after30Minute = Integer.toString(currentTime) + "30";
            baseTime = after30Minute;    //조회하고싶은 시간
        }
        String baseDate = "20210308";    //조회하고싶은 날짜
        String dataType = "json";    //타입 xml, json 등등 ..
        String numOfRows = "10000";    //한 페이지 결과 수

        //전날 23시 부터 153개의 데이터를 조회하면 오늘과 내일의 날씨를 알 수 있음

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));    /* 타입 */
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));    /* 한 페이지 결과 수 */

        // GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String data = sb.toString();

        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        //element 변수 선언
        JSONObject element;

//        System.out.println(data);
        System.out.println("baseTime = " + baseTime);

        //시간 확인
        GetTodayWeatherCalHour(baseTime);

        for (int i = 0; i < parse_item.size(); i++) {
            element = (JSONObject) parse_item.get(i);

            //시간 별 날씨 데이터 받아오기 함수 호출
            today(element, "0000", clock_00, "0시");
            today(element, "0100", clock_01, "1시");
            today(element, "0200", clock_02, "2시");
            today(element, "0300", clock_03, "3시");
            today(element, "0400", clock_04, "4시");
            today(element, "0500", clock_05, "5시");
            today(element, "0600", clock_06, "6시");
            today(element, "0700", clock_07, "7시");
            today(element, "0800", clock_08, "8시");
            today(element, "0900", clock_09, "9시");
            today(element, "1000", clock_10, "10시");
            today(element, "1100", clock_11, "11시");
            today(element, "1200", clock_12, "12시");

            today(element, "1300", clock_13, "13시");
            today(element, "1400", clock_14, "14시");
            today(element, "1500", clock_15, "15시");
            today(element, "1600", clock_16, "16시");
            today(element, "1700", clock_17, "17시");
            today(element, "1800", clock_18, "18시");
            today(element, "1900", clock_19, "19시");
            today(element, "2000", clock_20, "20시");
            today(element, "2100", clock_21, "21시");
            today(element, "2200", clock_22, "22시");
            today(element, "2300", clock_23, "23시");
            today(element, "2400", clock_00, "24시");


        }

        return todayWeahterResult;


    }

    //오늘의 날씨 1시간 단위로 불러오는 함수
    void today(JSONObject object,String clock,Map clockValue,String clockName){

        if(object.get("fcstTime").equals(clock)){
            if(object.get("category").equals("SKY")){
                String skyValue= object.get("fcstValue").toString();
                clockValue.put("SKY",skyValue);

            }
            else if(object.get("category").equals("PTY")){
                String ptyValue= object.get("fcstValue").toString();
                clockValue.put("PTY",ptyValue);
            }
            else if(object.get("category").equals("T1H")){
                String T1H= object.get("fcstValue").toString();
                clockValue.put("T1H",T1H);
            }


            todayWeahterResult.put(clockName,clockValue);

        }




    }

    //1시간 단위로 받아올 수 있는 최대 시간을 계산해 반환->3시간 단위로 받을 시각 계산위
    String GetTodayWeatherCalHour(String baseTime){
        String returnTimeValue=null;
        Integer beforeCalValue=Integer.parseInt(baseTime);
        //6시간 더해 반환
        if(baseTime.equals("0030")||baseTime.equals("0330")||baseTime.equals("0630")||baseTime.equals("0930")||baseTime.equals("1230")||baseTime.equals("1530")||baseTime.equals("1830")||baseTime.equals("2130")){

            Integer afterCalValue=beforeCalValue+400;
            if(afterCalValue>=2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            else{
                if(afterCalValue<1030&&afterCalValue>=0030)
                    returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }
        //5시간 더해 변환
        else if(baseTime.equals("0130")||baseTime.equals("0430")||baseTime.equals("0730")||baseTime.equals("1030")||baseTime.equals("1330")||baseTime.equals("1630")||baseTime.equals("1930")||baseTime.equals("2230")){

            Integer afterCalValue=beforeCalValue+400;
            if(afterCalValue>=2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="0"+afterCalValue;
            }
            else{
                if(afterCalValue<1030&&afterCalValue>=0030)
                    returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }

        //4시간 더해 변환
        else if(baseTime.equals("0230")||baseTime.equals("0530")||baseTime.equals("0830")||baseTime.equals("1030")||baseTime.equals("1430")||baseTime.equals("1730")||baseTime.equals("2030")||baseTime.equals("2330")){

            Integer afterCalValue=beforeCalValue+400;
            if(afterCalValue>=2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            else{
                if(afterCalValue<1030&&afterCalValue>=0030)
                    returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }

        return returnTimeValue;
    }



}
