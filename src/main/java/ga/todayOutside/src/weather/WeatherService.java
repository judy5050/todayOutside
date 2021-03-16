package ga.todayOutside.src.weather;


import ga.todayOutside.config.secret.Secret;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.weather.model.GetWeeklyReq;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;



@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {

    //todayWeather 시각별 날씨 관련 변수 선언

    //최종 오늘의 날씨 return 변수
    static Map<String, Map> todayWeahterResult = new LinkedHashMap<>();

    static Map<String, String> clock_00 = new HashMap<>();
    static Map<String, String> clock_01 = new HashMap<>();
    static Map<String, String> clock_02 = new HashMap<>();
    static Map<String, String> clock_03 = new HashMap<>();
    static Map<String, String> clock_04 = new HashMap<>();
    static Map<String, String> clock_05 = new HashMap<>();
    static Map<String, String> clock_06 = new HashMap<>();
    static Map<String, String> clock_07 = new HashMap<>();
    static Map<String, String> clock_08 = new HashMap<>();

    static Map<String, String> clock_09 = new HashMap<>();
    static Map<String, String> clock_10 = new HashMap<>();
    static Map<String, String> clock_11 = new HashMap<>();
    static Map<String, String> clock_12 = new HashMap<>();
    static Map<String, String> clock_13 = new HashMap<>();
    static Map<String, String> clock_14 = new HashMap<>();
    static Map<String, String> clock_15 = new HashMap<>();
    static Map<String, String> clock_16 = new HashMap<>();
    static Map<String, String> clock_17 = new ManagedMap<>();

    static Map<String, String> clock_18 = new HashMap<>();
    static Map<String, String> clock_19 = new HashMap<>();
    static Map<String, String> clock_20 = new HashMap<>();
    static Map<String, String> clock_21 = new HashMap<>();
    static Map<String, String> clock_22 = new HashMap<>();
    static Map<String, String> clock_23 = new HashMap<>();


    static Map<String, Map> day1Result = new LinkedHashMap<>();

    static Map<String, Map> day2Result = new LinkedHashMap<>();


    //일주일 단위의 날씨 정보 조회 변수

    static Map<String, String> day1_high = new HashMap<>();
    static Map<String, String> day1_low = new HashMap<>();

    static Map<String, String> day2_high = new HashMap<>();
    static Map<String, String> day2_low = new HashMap<>();

    static Map<String, String> day3_high = new HashMap<>();
    static Map<String, String> day3_low = new HashMap<>();

    static Map<String, String> day4_high = new HashMap<>();
    static Map<String, String> day4_low = new HashMap<>();

    static Map<String, String> day5_high = new HashMap<>();
    static Map<String, String> day5_low = new HashMap<>();

    static Map<String, String> day6_high = new HashMap<>();
    static Map<String, String> day6_low = new HashMap<>();

    static Map<String, String> day7_high = new HashMap<>();
    static Map<String, String> day7_low = new HashMap<>();

    //최종 오늘의 날씨 return 변수
    static Map<String, Map> todayWeatherPer03Result = new LinkedHashMap<>();


    //3시간별 단위 날씨 조회
    //3시간을 1시간 단위로 쪼갬

    static Map<String, String> per_00 = new HashMap<>();
    static Map<String, String> per_03 = new HashMap<>();
    static Map<String, String> per_06 = new HashMap<>();
    static Map<String, String> per_09 = new HashMap<>();
    static Map<String, String> per_12 = new HashMap<>();

    static Map<String, String> per_15 = new HashMap<>();
    static Map<String, String> per_18 = new HashMap<>();
    static Map<String, String> per_21 = new HashMap<>();


    //당일~7일 데이터 정보 넘김
    static Map<String, String> day_01_high = new HashMap<>();
    static Map<String, String> day_01_low = new HashMap<>();
    static Map<String, String> day_02_high = new HashMap<>();
    static Map<String, String> day_02_low = new HashMap<>();
    static Map<String, String> day_03_high = new HashMap<>();
    static Map<String, String> day_03_low = new HashMap<>();
    static Map<String, String> day_04_high = new HashMap<>();
    static Map<String, String> day_04_low = new HashMap<>();
    static Map<String, String> day_05_high = new HashMap<>();
    static Map<String, String> day_05_low = new HashMap<>();
    static Map<String, String> day_06_high = new HashMap<>();
    static Map<String, String> day_06_low = new HashMap<>();
    static Map<String, String> day_07_high = new HashMap<>();
    static Map<String, String> day_07_low = new HashMap<>();

    static Map<String, String> weeklyHighAndLowResult = new LinkedHashMap<>();// 키값 자동정

    //현재 날씨 결과
    static Map<String, String> nowWeatherResult = new HashMap<>();

    //전체 시간 데이터


    //합친 코드 확인용 결과값
    static Map<String, String> concat = new LinkedHashMap<>();

    // 하루용 데이터 총 개수 14개만 가져오기 위한 함수
    int count;

    //전날 날씨 데이터와 비교 하기 위한 저장값


    //오늘 최고 기온 최저기온 변수
    static Map<String, String> todayWeatherHighAndResult = new HashMap<>();

    //3~7일의 강수 확률 &날씨예보 결과 반환 코드
    static Map<String,String> weeklyWeatherForecastResult=new LinkedHashMap<>();
    static Map<String,String> weeklyRnForecastResult=new LinkedHashMap<>();
    static Map<String,String> weeklyForecastResult=new LinkedHashMap<>();

    /**
     * 시간 데이터 용
     */

    Calendar cal;
    Calendar yes;
    SimpleDateFormat sdf;


    //어제
    String yesterdayStr;


    //오늘
    String todayStr;
    Integer todayInteger;
    String todayDate;

    //내일
    String tomorrowStr;
    Integer tomorrowInteger;

    //모레
    String afterTomorrowStr;
    Integer afterTomorrowInteger;


    //나중에 유저에서 시 군 구.. 주소 값을 가져오기 위해 의존관계 주입
    private final UserInfoRepository userInfoRepository;

    //초단기 예보에서 최근 시간 정보만 받기 위한 값
    int todayWeatherNowCount=0;

    /**
     * 한시간 단위로 날씨 정보를 받아오는 함수
     */
    public void todayWeatherPer1Hour(String x,String y) throws IOException, ParseException {

        //시간을 받아오는 코드
        //조회하는 시간에서 +1 정보만 가져온다.
        Integer currentTime = LocalDateTime.now().getHour();
        Integer min = LocalDateTime.now().getMinute();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = x;    //위도
        String ny = y;    //경도
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
            } else {
                Integer.toString(calTime);
                String before30Minute = calTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간
                System.out.println("##########");
            }


        } else {
            if (currentTime < 10) {
                Integer.toString(currentTime);
                String before30Minute = "0" + currentTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간

            } else {
                String after30Minute = Integer.toString(currentTime) + "30";
                baseTime = after30Minute;    //조회하고싶은 시간
            }
        }
        System.out.println("baseTime = " + baseTime);
        System.out.println("todayStr =  "+ todayStr);
        String baseDate = todayStr;    //조회하고싶은 날짜
        String dataType = "json";    //타입 xml, json 등등 ..
        String numOfRows = "50";    //한 페이지 결과 수

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

        //시간 확인
        getTodayWeatherMaxHour(baseTime);
        System.out.println("parse_body = " + parse_body);
        for (int i = 0; i < parse_item.size(); i++) {
            element = (JSONObject) parse_item.get(i);

            //시간 별 날씨 데이터 받아오기 함수 호출
            today1HourWeatherParsing(element, "0000", clock_00, "0시");
            today1HourWeatherParsing(element, "0100", clock_01, "1시");
            today1HourWeatherParsing(element, "0200", clock_02, "2시");
            today1HourWeatherParsing(element, "0300", clock_03, "3시");
            today1HourWeatherParsing(element, "0400", clock_04, "4시");
            today1HourWeatherParsing(element, "0500", clock_05, "5시");
            today1HourWeatherParsing(element, "0600", clock_06, "6시");
            today1HourWeatherParsing(element, "0700", clock_07, "7시");
            today1HourWeatherParsing(element, "0800", clock_08, "8시");
            today1HourWeatherParsing(element, "0900", clock_09, "9시");
            today1HourWeatherParsing(element, "1000", clock_10, "10시");
            today1HourWeatherParsing(element, "1100", clock_11, "11시");
            today1HourWeatherParsing(element, "1200", clock_12, "12시");

            today1HourWeatherParsing(element, "1300", clock_13, "13시");
            today1HourWeatherParsing(element, "1400", clock_14, "14시");
            today1HourWeatherParsing(element, "1500", clock_15, "15시");
            today1HourWeatherParsing(element, "1600", clock_16, "16시");
            today1HourWeatherParsing(element, "1700", clock_17, "17시");
            today1HourWeatherParsing(element, "1800", clock_18, "18시");
            today1HourWeatherParsing(element, "1900", clock_19, "19시");
            today1HourWeatherParsing(element, "2000", clock_20, "20시");
            today1HourWeatherParsing(element, "2100", clock_21, "21시");
            today1HourWeatherParsing(element, "2200", clock_22, "22시");
            today1HourWeatherParsing(element, "2300", clock_23, "23시");
            today1HourWeatherParsing(element, "2400", clock_00, "24시");


        }


    }

    /**
     * 1시간 단위로 오늘 날씨 데이터에서 원하는 값만 파싱해 사용하기 위한 함
     */
    void today1HourWeatherParsing(JSONObject object, String clock, Map clockValue, String clockName) {

        if (object.get("fcstTime").equals(clock)) {
            if (object.get("category").equals("SKY")) {
                String skyValue = object.get("fcstValue").toString();
                clockValue.put("SKY", skyValue);


            } else if (object.get("category").equals("PTY")) {
                String ptyValue = object.get("fcstValue").toString();
                clockValue.put("PTY", ptyValue);
                count++;
            } else if (object.get("category").equals("T1H")) {
                String T1H = object.get("fcstValue").toString();
                clockValue.put("T1H", T1H);
            }


            todayWeahterResult.put(clockName, clockValue);

        }


    }

    /**
     * 1시간 단위로 받아올 수 있는 최대 시간을 계산해 반환->3시간 단위로 받을 시각 계산위해
     */
    String getTodayWeatherMaxHour(String baseTime) {

        String returnTimeValue = null;
        Integer beforeCalValue = Integer.parseInt(baseTime);
        //6시간 더해 반환
        if (baseTime.equals("0030") || baseTime.equals("0330") || baseTime.equals("0630") || baseTime.equals("0930") || baseTime.equals("1230") || baseTime.equals("1530") || baseTime.equals("1830") || baseTime.equals("2130")) {

            Integer afterCalValue = beforeCalValue + 600;
            System.out.println("afterCalValue = " + afterCalValue);
            if (afterCalValue > 2430) {
                afterCalValue = afterCalValue - 2400;
                returnTimeValue = "0" + Integer.toString(afterCalValue);
            } else if (afterCalValue == 2430) {
                afterCalValue = afterCalValue - 2400;
                returnTimeValue = "00" + Integer.toString(afterCalValue);
            } else {
                if (afterCalValue < 1030 && afterCalValue >= 0030)
                    returnTimeValue = "0" + Integer.toString(afterCalValue);
                else {
                    returnTimeValue = Integer.toString(afterCalValue);
                }
            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }
        //5시간 더해 변환
        else if (baseTime.equals("0130") || baseTime.equals("0430") || baseTime.equals("0730") || baseTime.equals("1030") || baseTime.equals("1330") || baseTime.equals("1630") || baseTime.equals("1930") || baseTime.equals("2230")) {

            Integer afterCalValue = beforeCalValue + 500;
            if (afterCalValue >= 2430) {
                afterCalValue = afterCalValue - 2400;
                returnTimeValue = "0" + afterCalValue;
            } else if (afterCalValue < 1030 && afterCalValue > 0030) {

                returnTimeValue = "0" + Integer.toString(afterCalValue);
            } else {
                returnTimeValue = Integer.toString(afterCalValue);

            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }

        //4시간 더해 변환
        else if (baseTime.equals("0230") || baseTime.equals("0530") || baseTime.equals("0830") || baseTime.equals("1130") || baseTime.equals("1430") || baseTime.equals("1730") || baseTime.equals("2030") || baseTime.equals("2330")) {

            Integer afterCalValue = beforeCalValue + 400;
            if (afterCalValue >= 2430) {
                afterCalValue = afterCalValue - 2400;
                returnTimeValue = "0" + Integer.toString(afterCalValue);
            } else {
                if (afterCalValue < 1030 && afterCalValue >= 0030)
                    returnTimeValue = "0" + Integer.toString(afterCalValue);
                else {
                    returnTimeValue = Integer.toString(afterCalValue);
                }
            }
            System.out.println("returnTimeValue = " + returnTimeValue);
            return returnTimeValue;
        }

        return returnTimeValue;
    }

    //하늘상태, 강수확률,강수형, 최고 ,최고 기온
    //SKY,POP,PTY
    //getMapping 네이밍 생각해보기
    //3시간 마다 날씨 정보 제공
//    @ResponseBody
//    @GetMapping("/per3today")
    public void weatherPer3Hour(String x,String y) throws IOException, ParseException {

        /**
         * 시간 관련
         */

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //시각을 얻는 코드
        int t = LocalDateTime.now().getHour();
        System.out.println("currentTime = " + t);

        //1시간 단위와 동일한 시간 값 가져오기
        Integer valueMin = LocalDateTime.now().getMinute();
        String value = null;
        Integer calTime = null;


        //분에 따른 baseTime 조절
        if (valueMin <= 30) {

            if (t == 0) {
                calTime = 23;
            } else {
                calTime = t - 1;
            }
            if (calTime < 10) {
                Integer.toString(calTime);
                String before30Minute = "0" + calTime + "30";
                value = before30Minute;    //조회하고싶은 시간
            } else {
                Integer.toString(calTime);
                String before30Minute = calTime + "30";
                value = before30Minute;    //조회하고싶은 시간

            }

        } else {
            if (t < 10) {
                Integer.toString(t);
                String before30Minute = "0" + t + "30";
                value = before30Minute;    //조회하고싶은 시간
                System.out.println("value = " + value);

            } else {
                String after30Minute = Integer.toString(t) + "30";
                value = after30Minute;    //조회하고싶은 시간
            }

        }
        System.out.println("value = " + value);
        String cp = getTodayWeatherMaxHour(value);
        System.out.println("cmp = " + cp);
        String baseDate = "";    //조회하고싶은 날짜
        String baseTime="";
        if(t<20){
            baseTime = "2000";    //API 제공 시간
            baseDate=yesterdayStr;
        }
        else{
            baseTime="2000";
            baseDate=todayStr;

        }


        //시간 비교 위해 시간 데이터 변형
        // 0000~0900 fcstTime 용 데이터 변형
        String getTime = null;
        if (t >= 0 && t < 10) {
            getTime = "0" + t + "00";
        } else {
            getTime = t + "00";
        }
        System.out.println("getTime = " + getTime);


        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";    //동네예보조회

        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = x;    //위도
        String ny = y;    //경도

        //시간을 받아오는 코드
        int currentTime = LocalDateTime.now().getHour();
        int min = LocalDateTime.now().getMinute();

        String time = Integer.toString(currentTime) + Integer.toString(min);


        String dataType = "json";    //타입 xml, json
        String numOfRows = "256";    //한 페이지 결과 수
        //79일경우 딱 겹치지 x는 하루 시간 조회 가능

        //동네예보 -- 전날 05시 부터 225개의 데이터를 조회하면 모레까지의 날씨를 알 수 있음

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
        //JSONObject item = (JSONObject) parse_item.get("item");

        JSONObject object;

        //element 변수 선언
        JSONObject element = null;

        System.out.println("cp = " + cp);
        System.out.println(data);
        for (int i = 0; i < parse_item.size(); i++) {
            if (count < 14) {


                object = (JSONObject) parse_item.get(i);
                today3HourWeatherParsing(object, "0000", per_00, "00시", cp);
                today3HourWeatherParsing(object, "0300", per_03, "3시", cp);
                today3HourWeatherParsing(object, "0600", per_06, "6시", cp);
                today3HourWeatherParsing(object, "0900", per_09, "9시", cp);
                today3HourWeatherParsing(object, "1200", per_12, "12시", cp);
                today3HourWeatherParsing(object, "1500", per_15, "15시", cp);
                today3HourWeatherParsing(object, "1800", per_18, "18시", cp);
                today3HourWeatherParsing(object, "2100", per_21, "21시", cp);


            }


        }

//        return todayWeahterResult;
    }

    /**
     * 3시간 단위로 날씨값을 얻어오기 위한 함수
     */
    void today3HourWeatherParsing(JSONObject object, String clock, Map clockValue, String clockName, String clockCmp) {
        //오늘 일때
        if (object.get("fcstDate").equals(todayStr)) {
            if (Integer.parseInt(clockCmp) < Integer.parseInt(clock)) {
                if (object.get("fcstTime").equals(clock)) {
                    if (object.get("category").equals("SKY")) {
                        System.out.println("count = " + count);
                        String skyValue = object.get("fcstValue").toString();
                        clockValue.put("SKY", skyValue);
                    } else if (object.get("category").equals("PTY")) {
                        String ptyValue = object.get("fcstValue").toString();
                        clockValue.put("PTY", ptyValue);
                    } else if (object.get("category").equals("T3H")) {
                        System.out.println("object = " + object);
                        count++;
                        String T3H = object.get("fcstValue").toString();
                        clockValue.put("T3H", T3H);
                    }
                    todayWeahterResult.put(clockName, clockValue);
                }
            }
        } else if (object.get("fcstDate").equals(tomorrowStr)) {
            if (object.get("fcstTime").equals(clock)) {
                if (object.get("category").equals("SKY")) {
//                    System.out.println("tomorrowStr = " + tomorrowStr);
                    String skyValue = object.get("fcstValue").toString();
                    clockValue.put("SKY", skyValue);
                } else if (object.get("category").equals("PTY")) {
                    String ptyValue = object.get("fcstValue").toString();
                    clockValue.put("PTY", ptyValue);
                } else if (object.get("category").equals("T3H")) {
                    count++;
                    System.out.println("count = " + count);
                    System.out.println("object = " + object);
                    String T3H = object.get("fcstValue").toString();
                    clockValue.put("T3H", T3H);
                }
                day1Result.put(clockName, clockValue);
            }


        } else if (object.get("fcstDate").equals(afterTomorrowStr)) {
            if (object.get("fcstTime").equals(clock)) {
                if (object.get("category").equals("SKY")) {
                    String skyValue = object.get("fcstValue").toString();
                    clockValue.put("SKY", skyValue);
                } else if (object.get("category").equals("PTY")) {
                    String ptyValue = object.get("fcstValue").toString();
                    clockValue.put("PTY", ptyValue);
                } else if (object.get("category").equals("T3H")) {
                    count++;
                    System.out.println("count = " + count);
                    String T3H = object.get("fcstValue").toString();
                    clockValue.put("T3H", T3H);
                }
                day2Result.put(clockName, clockValue);
            }

        }


    }

    /**
     * service 에서 사용되는 날짜 값을 가져오기 위한 함수
     * 이름 바뀌기전 test
     */
    public void date() {

        cal = Calendar.getInstance();
        cal.setTime(new Date());

        yes = Calendar.getInstance();
        yes.setTime(new Date());

        sdf = new SimpleDateFormat("yyyyMMdd");
        todayStr = sdf.format(cal.getTime());
        todayInteger = Integer.parseInt(todayStr);

        cal.add(Calendar.DATE, +1);
        tomorrowStr = sdf.format(cal.getTime());
        tomorrowInteger = Integer.parseInt(tomorrowStr);

        cal.add(Calendar.DATE, +1);
        afterTomorrowStr = sdf.format(cal.getTime());
        afterTomorrowInteger = Integer.parseInt(afterTomorrowStr);

        yes.add(Calendar.DATE, -1);
        yesterdayStr = sdf.format(yes.getTime());

        LocalDateTime localDateTime = LocalDateTime.now();
        todayDate = Integer.toString(localDateTime.getDayOfMonth());


    }


    public Map getTodayWeatherList(String x,String y) throws IOException, ParseException {

        //오늘 날짜 받아오기

        date();
        todayWeatherPer1Hour(x,y);
        weatherPer3Hour(x,y);

        Map<String, Map> hashMap1 = new LinkedHashMap();
        Map<String, Map> hashMap2 = new LinkedHashMap();
        Map<String, Map> hashMap3 = new LinkedHashMap();


        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();

        jsonObject1.put("today", todayWeahterResult);
        jsonObject2.put("tomorrow", day1Result);
        jsonObject3.put("afterTomorrow", day2Result);


        hashMap1.put("today", todayWeahterResult);
        hashMap2.put("tomorrow", day1Result);
        hashMap1.putAll(hashMap2);
        if (!day2Result.isEmpty()) {
            hashMap3.put("afterTomorrow", day2Result);
            hashMap1.putAll(hashMap3);

        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(hashMap1);

        return hashMap1;


    }


    /**
     * 현재 날씨 조회하기 (초단기 실황 api 작성)
     * 변경
     */

//    Map getTodayWeatherNow() throws IOException, ParseException {
//
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        LocalDateTime time = LocalDateTime.now();
//        System.out.println("time = " + time.getHour());
//        String todayWeatherBaseDate = sdf.format(date);
//        System.out.println("todayWeatherBaseDate = " + todayWeatherBaseDate);
//
//        Integer todayWeatherNowHour = LocalDateTime.now().getHour();
//        Integer todayWeatherNowMinute = LocalDateTime.now().getMinute();
//        String todayWeatherNowBaseTime = null;
//
//
//        //api 제공 시간이 매 시간 40분 이후 부터
//        //넉넉하게 5분 여유 시간
//        //45분 전 일경우 한시간 전을 baseTime으로 사용
//        System.out.println("todayWeatherNowMinute = " + todayWeatherNowMinute);
//        if (todayWeatherNowMinute < 45) {
//            if (todayWeatherNowHour == 0) {
//                todayWeatherNowHour = 23;
//
//            } else {
//                System.out.println("todayWeatherNowHour = " + todayWeatherNowHour);
//                todayWeatherNowHour -= 1;
//                System.out.println("todayWeatherNowHour = " + todayWeatherNowHour);
//            }
////            System.out.println("todayWeatherNowHour = " + todayWeatherNowHour);
//
//            if (todayWeatherNowHour < 10) {
//                todayWeatherNowBaseTime = "0" + todayWeatherNowHour + "00";
//            } else {
//                todayWeatherNowBaseTime = todayWeatherNowHour + "00";
//            }
//        } else {
//            if (todayWeatherNowHour < 10) {
//                todayWeatherNowBaseTime = "0" + todayWeatherNowHour + "00";
//            } else {
//                todayWeatherNowBaseTime = todayWeatherNowHour + "00";
//            }
//        }
//        System.out.println("todayWeatherNowBaseTime = " + todayWeatherNowBaseTime);
//
//
//        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";    //초단기실황조회
//
//        // 홈페이지에서 받은 키
//        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
//        String nx = "60";    //위도
//        String ny = "127";    //경도
//        String baseDate = todayWeatherBaseDate;    //조회하고싶은 날짜
//        String baseTime = todayWeatherNowBaseTime;    //API 제공 시간
//        String dataType = "json";    //타입 xml, json
//        String numOfRows = "250";    //한 페이지 결과 수
//
//        //동네예보 -- 전날 05시 부터 225개의 데이터를 조회하면 모레까지의 날씨를 알 수 있음
//
//        StringBuilder urlBuilder = new StringBuilder(apiUrl);
//        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
//        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
//        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
//        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
//        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
//        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));    /* 타입 */
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));    /* 한 페이지 결과 수 */
//
//        // GET방식으로 전송해서 파라미터 받아오기
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//
//        BufferedReader rd;
//        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        String data = sb.toString();
//
//        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
//        JSONParser parser = new JSONParser();
//        JSONObject obj = (JSONObject) parser.parse(data);
//        // response 키를 가지고 데이터를 파싱
//        JSONObject parse_response = (JSONObject) obj.get("response");
//        // response 로 부터 body 찾기
//        JSONObject parse_body = (JSONObject) parse_response.get("body");
//        // body 로 부터 items 찾기
//        JSONObject parse_items = (JSONObject) parse_body.get("items");
//        JSONArray parse_item = (JSONArray) parse_items.get("item");
//        //JSONObject item = (JSONObject) parse_item.get("item");
//
//        //parsing 용 object
//        JSONObject object = new JSONObject();
//        System.out.println(parse_item);
//        System.out.println("parse_item = " + parse_item.size());
//        for (int i = 0; i < parse_item.size(); i++) {
//            object = (JSONObject) parse_item.get(i);
//            System.out.println("object = " + object);
//            todayNowWeatherParsing(object);
//
//
//        }
//
//        return nowWeatherResult;
//    }
//전

    /**
     * 현재 날씨 조회하기(초단기 예보) 변경후
     */


    Map getTodayWeatherNow(String x,String y) throws IOException, ParseException {


        date();
        String fcstTime=null;
        //시간을 받아오는 코드
        //조회하는 시간에서 +1 정보만 가져온다.
        Integer currentTime = LocalDateTime.now().getHour();
        Integer min = LocalDateTime.now().getMinute();


        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = x;    //위도
        String ny = y;    //경도
        String baseTime = null;

        //분에 따른 baseTime 조절
        if (min <= 45) {
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
                fcstTime="0"+currentTime.toString()+"00";
            } else {
                Integer.toString(calTime);
                String before30Minute = calTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간
                fcstTime=currentTime.toString()+"00";
            }


        } else {
            if (currentTime < 10) {
                Integer currentTimePlus=currentTime+1;
                Integer.toString(currentTime);
                String before30Minute = "0" + currentTime + "30";
                baseTime = before30Minute;    //조회하고싶은 시간
                fcstTime="0"+currentTime.toString()+"00";

            } else {
                String after30Minute = Integer.toString(currentTime) + "30";
                baseTime = after30Minute;    //조회하고싶은 시간
                fcstTime=currentTime.toString()+"00";
            }
        }

        //분에 따른 fcstTime 조절
        Integer convertFcstTime=currentTime;
        if (min <30) {

            if (currentTime == 0) {
                convertFcstTime = 24;
            } else {
                convertFcstTime = currentTime;
            }
            if (convertFcstTime < 10) {
                fcstTime="0"+convertFcstTime+"00";
            } else {
                fcstTime=convertFcstTime+"00";
            }


        } else {
            convertFcstTime=currentTime+1;
            if (convertFcstTime < 10) {

                fcstTime="0"+currentTime+"00";

            } else {
                fcstTime=convertFcstTime+"00";
            }
        }

        System.out.println("fcstTime = " + fcstTime);
        System.out.println("baseTime = " + baseTime);
        System.out.println("todayStr =  "+ todayStr);
        String baseDate = todayStr;    //조회하고싶은 날짜
        String dataType = "json";    //타입 xml, json 등등 ..
        String numOfRows = "50";    //한 페이지 결과 수

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

        //시간 확인
        getTodayWeatherMaxHour(baseTime);
        System.out.println("fcstTime = " + fcstTime);
        System.out.println("parse_body = " + parse_body);
        for (int i = 0; i < parse_item.size(); i++) {
            element = (JSONObject) parse_item.get(i);

            //시간 별 날씨 데이터 받아오기 함수 호출
            System.out.println("element = " + element);
            todayNowWeatherParsing(element,fcstTime);

            //최신 값 하나만 받고 끝내기 위한 변수 와 로직
            if(todayWeatherNowCount>=1){
                break;
            }

        }

        return nowWeatherResult;
    }



    /**
     * 오늘의 날씨 정보(현재 날씨에 대해 원하는 값만 파싱해서 가져오기)
     * 변경전 (초단기 실황)
     */

//    void todayNowWeatherParsing(JSONObject object) {
//
//        if (object.get("category").equals("T1H")) {
//            String T1H = object.get("obsrValue").toString();
//            nowWeatherResult.put("T1H", T1H);
//
//
//        } else if (object.get("category").equals("PTY")) {
//            String ptyValue = object.get("obsrValue").toString();
//            nowWeatherResult.put("PTY", ptyValue);
//
//        }
//
//
//    }


    /**
     * 오늘의 날씨 정보(현재 날씨에 대해 원하는 값만 파싱해서 가져오기)
     * 변경후 (초단기 예보)
     */
    void todayNowWeatherParsing(JSONObject object,String fcstTime) {

            if(object.get("fcstTime").equals(fcstTime))
            {
                if (object.get("category").equals("T1H")) {
                    String T1H = object.get("fcstValue").toString();
                    nowWeatherResult.put("T1H", T1H);
                    todayWeatherNowCount++;
                    System.out.println("todayWeatherNowCount = " + todayWeatherNowCount);



                } else if (object.get("category").equals("PTY")) {
                    String ptyValue = object.get("fcstValue").toString();
                    nowWeatherResult.put("PTY", ptyValue);

                }

                else if (object.get("category").equals("SKY")) {
                    String ptyValue = object.get("fcstValue").toString();
                    nowWeatherResult.put("SKY", ptyValue);

                }
            }





    }

    /**
     * 오늘 날씨 최고 최저 기온 파싱 하는 함수
     */
    //전날 23시 조회 기준
    //최고기온은 15시 데이터
    //최저 기온은 6시 데이터
    void todayWeatherHighAndLowParsing(JSONObject object) {
        if (object.get("fcstDate").equals(todayStr)) {
            if (object.get("category").equals("TMN")) {
                System.out.println("object = " + object);
                String skyValue = object.get("fcstValue").toString();
                todayWeatherHighAndResult.put("TMN", skyValue);
            } else if (object.get("category").equals("TMX")) {
                System.out.println("object = " + object);
                String ptyValue = object.get("fcstValue").toString();
                todayWeatherHighAndResult.put("TMX", ptyValue);

            }
        }

    }


    /**
     * 오늘의 최고기온 최저 기온 조회
     * 전날 23시를 basetime 으로  그다음 날 최고, 최저 기온을 조회한다.
     */
    public Map getTodayWeatherHighAndLow(String x,String y) throws IOException, ParseException {


        date();
        System.out.println("yesterdayStr = " + yesterdayStr);
        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";    //동네예보조회

        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = x;    //위도
        String ny = y;    //경도

        //시간을 받아오는 코드
        int currentTime = LocalDateTime.now().getHour();
        int min = LocalDateTime.now().getMinute();

        String time = Integer.toString(currentTime) + Integer.toString(min);

        String baseTime = "2300";    //API 제공 시간
        String baseDate = yesterdayStr;    //조회하고싶은 날짜
        System.out.println("yesterdayStr = " + yesterdayStr);
        String dataType = "json";    //타입 xml, json
        String numOfRows = "256";    //한 페이지 결과 수
        //79일경우 딱 겹치지 x는 하루 시간 조회 가능

        //동네예보 -- 전날 05시 부터 225개의 데이터를 조회하면 모레까지의 날씨를 알 수 있음

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
        //JSONObject item = (JSONObject) parse_item.get("item");

        JSONObject object;

        //element 변수 선언
        JSONObject element = null;

        for (int i = 0; i < parse_item.size(); i++) {
            {
                object = (JSONObject) parse_item.get(i);
                todayWeatherHighAndLowParsing(object);
            }


        }

        return todayWeatherHighAndResult;
    }


    /**
     * 중기 최고, 최저 기온 조회를 위한 예보지역 코드 변경을 위한 코드
     */


    public String convertForWeeklyHighAndLowWeather(String req) throws ParseException, IOException {
        date();
        System.out.println("todayDate = " + todayDate);
        ClassPathResource resource = new ClassPathResource("static/check.json");
        JSONArray json = (JSONArray) new JSONParser().parse(new InputStreamReader(resource.getInputStream(), "UTF-8")); //json-simple
        JSONObject jsonObject;
        String localCode = null;

        for (int i = 0; i < json.size(); i++) {
            jsonObject = (JSONObject) json.get(i);
            if (jsonObject.get("도시").toString().matches(".*" + req + ".*")) {
                localCode = jsonObject.get("코드").toString();
            }

        }

        return localCode;
    }

    /**
     * 중기 최고 최저 기온
     * 그 다음날 6시가 되기 이전에는 그 전날의 날 기준 3~10일 데이터 값을 받아옴
     *
     */

    public Map weeklyHighAndLow(String localCode) throws IOException, ParseException {
        int index=0;// currentTime 에 따른 일주일 값 받아오는 코드의 차이를 위해 추가한 코드
                    //currentTime<6일경우 전날의 데이터 값을 받아오기 때문에 +1개의 데이터 필요



        //시각을 얻는 코드
        int currentTime = LocalDateTime.now().getHour();
        System.out.println("currentTime = " + currentTime);

        String date;
        if(currentTime<6){
            date=yesterdayStr;
        }
        else{
            date=todayStr;
        }


        //날짜 값 빼기 연산
//        cal.add(Calendar.DATE,-1);

        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";    //중기기온조회
        System.out.println("localCode = " + localCode);
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String regId = localCode;    //예보 구역 코드

        //발표시각 0600 1800시

        String tmFc = date+ "0600";    //발표시각 입력
        String dataType = "json";    //타입 xml, json
        String numOfRows = "250";    //한 페이지 결과 수

        System.out.println("tmFc = " + tmFc);

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));    /* 타입 */

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
        //JSONObject item = (JSONObject) parse_item.get("item");
        JSONObject object;
        System.out.println("parse_items = " + parse_items);
        for (int i=0;i<parse_item.size();i++){
             object = (JSONObject) parse_item.get(i);
            System.out.println("parse_item = " + parse_item.get(0));
            weekHighAndLowParsing(object);

        }

        return weeklyHighAndLowResult;


    }

    /**
     * 중기 기온 최고, 최저 파싱 함수
     */
    void weekHighAndLowParsing(JSONObject object) {
        String day2HighResult = object.get("taMax3").toString();
        weeklyHighAndLowResult.put("taMax2", day2HighResult);
        String day2LowResult = object.get("taMin3").toString();
        weeklyHighAndLowResult.put("taMin2", day2LowResult);

        String day3HighResult = object.get("taMax4").toString();
        weeklyHighAndLowResult.put("taMax3", day3HighResult);
        String day3LowResult = object.get("taMin4").toString();
        weeklyHighAndLowResult.put("taMin3", day3LowResult);

        String day4HighResult = object.get("taMax5").toString();
        weeklyHighAndLowResult.put("taMax4", day4HighResult);
        String day4LowResult = object.get("taMin5").toString();
        weeklyHighAndLowResult.put("taMin4", day4LowResult);

        String day5HighResult = object.get("taMax6").toString();
        weeklyHighAndLowResult.put("taMax5", day5HighResult);
        String day5LowResult = object.get("taMin6").toString();
        weeklyHighAndLowResult.put("taMin5", day5LowResult);


        String day6HighResult = object.get("taMax7").toString();
        weeklyHighAndLowResult.put("taMax6", day6HighResult);
        String day6LowResult = object.get("taMin7").toString();
        weeklyHighAndLowResult.put("taMin6", day6LowResult);


        String day7HighResult = object.get("taMax8").toString();
        weeklyHighAndLowResult.put("taMax7", day7HighResult);
        String day7LowResult = object.get("taMin8").toString();
        weeklyHighAndLowResult.put("taMin7", day7LowResult);


    }

    /**
     * 중기 강수량 및 일기예보 조회를 위한 예보지역 코드 변경을 위한 코드
     */


    public String convertForWeeklyWeatherForeCast(String req) throws ParseException, IOException {
        date();
        System.out.println("todayDate = " + todayDate);
        ClassPathResource resource = new ClassPathResource("static/forecastAreaCode.json");
        JSONArray json = (JSONArray) new JSONParser().parse(new InputStreamReader(resource.getInputStream(), "UTF-8")); //json-simple
        JSONObject jsonObject;
        String localCode = null;

        for (int i = 0; i < json.size(); i++) {
            jsonObject = (JSONObject) json.get(i);
            if (jsonObject.get("구역").toString().matches(".*" + req + ".*")) {
                localCode = jsonObject.get("예보구역코드").toString();
            }

        }

        return localCode;
    }

    /**
     * 중기 강수확률 및 날씨 예보 정보 가져오기
     */


    public Map weeklyForecastResult(String localCode) throws IOException, ParseException {

        //시각을 얻는 코드
        int currentTime = LocalDateTime.now().getHour();
        System.out.println("currentTime = " + currentTime);

        String date;
        if(currentTime<6){
            date=yesterdayStr;
        }
        else{
            date=todayStr;
        }


        //날짜 값 빼기 연산
//        cal.add(Calendar.DATE,-1);

        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";    //중기기온조회
        System.out.println("localCode = " + localCode);
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String regId = localCode;    //예보 구역 코드

        //발표시각 0600 1800시

        String tmFc = date+ "0600";    //발표시각 입력
        String dataType = "json";    //타입 xml, json
        String numOfRows = "250";    //한 페이지 결과 수

        System.out.println("tmFc = " + tmFc);

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));    /* 타입 */

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
        //JSONObject item = (JSONObject) parse_item.get("item");
        JSONObject object;
        System.out.println("parse_items = " + parse_items);
        for (int i=0;i<parse_item.size();i++){
            object = (JSONObject) parse_item.get(i);
            System.out.println("parse_item = " + parse_item.get(0));
            weekForecastParsing(object);

        }

        return weeklyForecastResult;


    }

    /**
     * 중기 기온 최고, 최저 파싱 함수
     */
    void weekForecastParsing(JSONObject object) {
        String day2RnResult = object.get("rnSt3Am").toString();
        weeklyRnForecastResult.put("rnSt2Am", day2RnResult);

        String day3RnResult = object.get("rnSt4Am").toString();
        weeklyRnForecastResult.put("rnSt3Am", day3RnResult);

        String day4RnResult = object.get("rnSt5Am").toString();
        weeklyRnForecastResult.put("rnSt4Am", day4RnResult);

        String day5RnResult = object.get("rnSt6Am").toString();
        weeklyRnForecastResult.put("rnSt5Am", day5RnResult);

        String day6RnResult = object.get("rnSt7Am").toString();
        weeklyRnForecastResult.put("rnSt6Am", day6RnResult);

        String day7RnResult = object.get("rnSt8").toString();
        weeklyRnForecastResult.put("rnSt8Am", day7RnResult);


        //날씨 예보
        String day2WeatherResult = object.get("wf3Am").toString();
        weeklyWeatherForecastResult.put("wf2Am", day2WeatherResult);

        String day3WeatherResult = object.get("wf4Am").toString();
        weeklyWeatherForecastResult.put("wf3Am", day3WeatherResult);

        String day4WeatherResult = object.get("wf5Am").toString();
        weeklyWeatherForecastResult.put("wf4Am", day4WeatherResult);

        String day5WeatherResult = object.get("wf6Am").toString();
        weeklyWeatherForecastResult.put("wf5Am", day5WeatherResult);

        String day6WeatherResult = object.get("wf7Am").toString();
        weeklyWeatherForecastResult.put("wf6Am", day6WeatherResult);

        String day7WeatherResult = object.get("wf8").toString();
        weeklyWeatherForecastResult.put("wf7Am", day7WeatherResult);



        weeklyForecastResult.putAll(weeklyWeatherForecastResult);
        weeklyForecastResult.putAll(weeklyRnForecastResult);


    }

    //사용자 위치 값 nx,ny로 변경

    public Map<String, String> converNxNy(String firstAddressName, String secondAddressName) throws IOException, ParseException {

//        String test="금천구";
             String result;
//        String areaTop="서울특별시";	//지역
//        String areaMdl="금천구";
//        String areaLeaf="종로1가동";
        String areaTop=firstAddressName;	//지역
        String areaMdl=secondAddressName;

        String code="";	//지역 코드
        String x="";
        String y="";

        Map<String,String> returnValue=new HashMap<>();


        URL url;
        BufferedReader br;
        URLConnection conn;

        JSONParser parser;
        JSONArray jArr;
        JSONObject jobj;

        //시 검색
        result=null;
        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            result = br.readLine().toString();
            br.close();
        }catch (NullPointerException e){
            System.out.println(e);
        }

        System.out.println(result);

        jArr=null;
        //시에 맞는 코드를 가져오는 코드
        parser = new JSONParser();
        try{
            jArr = (JSONArray) parser.parse(result);
        }catch (NullPointerException e){
            System.out.println("e = " + e);
        }


        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
            if(jobj.get("value").equals(areaTop)) {
                code=(String)jobj.get("code");
                System.out.println(areaTop+"코드 : "+code);
                break;
            }
        }

        //구 검색
        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+code+".json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = br.readLine().toString();
        br.close();
        //System.out.println(result);

        parser = new JSONParser();
        jArr = (JSONArray) parser.parse(result);

        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
            if(jobj.get("value").equals(areaMdl)) {
                code=(String)jobj.get("code");
                System.out.println("jobj = " + jobj);
                System.out.println(areaMdl+"코드 : "+code);
                break;
            }
        }

        System.out.println("code = " + code);
        //동 검색
        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+code+".json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = br.readLine().toString();
        br.close();
        //System.out.println(result);

        parser = new JSONParser();
        jArr = (JSONArray) parser.parse(result);

//        if(areaMdl.equals(test)) {
        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
//                System.out.println("jArr.size() = " + jArr.size());
            System.out.println("jobj = " + jobj);
//                String leaf1=areaLeaf.substring(0,areaLeaf.length()-3);
//                String leaf2=areaLeaf.substring(areaLeaf.length()-3,areaLeaf.length()-2);
//                String leaf3=areaLeaf.substring(areaLeaf.length()-2,areaLeaf.length());

//                Pattern pattern = Pattern.compile(leaf1+"[1-9.]{0,8}"+leaf2+"[1-9.]{0,8}"+leaf3);
//                Matcher matcher = pattern.matcher((String) jobj.get("value"));
//                if(matcher.find()) {
            x=(String)jobj.get("x");
            y=(String)jobj.get("y");

            System.out.println("x값 : "+x+", y값 :"+y);
            break;
        }

        //리턴 값에 nx ny값 추가
        returnValue.put("x",x);
        returnValue.put("y",y);
//            }
//        }else {
//            for(int i = 0 ; i < jArr.size(); i++) {
//                jobj = (JSONObject) jArr.get(i);
//                if(jobj.get("value").equals(areaLeaf)) {
//                    x=(String)jobj.get("x");
//                    y=(String)jobj.get("y");
//                    System.out.println(areaLeaf+"의 x값 : "+x+", y값 :"+y);
//                    break;
//                }
//            }
//        }


        //좌표값 반환
        return  returnValue;
    }




}