package ga.todayOutside.src.weather;


import ga.todayOutside.config.secret.Secret;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@RestController
public class WeatherTest {

    //todayWeather 시각별 날씨 관련 변수 선언

    //최종 오늘의 날씨 return 변수
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

    //날씨 14개의 데이터를 제공하기 위해 만든 변수
    static Map<String,String> day1_00=new HashMap<>();
    static Map<String,String> day1_03=new HashMap<>();
    static Map<String,String> day1_06=new HashMap<>();
    static Map<String,String> day1_09=new HashMap<>();
    static Map<String,String> day1_12=new HashMap<>();
    static Map<String,String> day1_15=new HashMap<>();
    static Map<String,String> day1_18=new HashMap<>();
    static Map<String,String> day1_21=new HashMap<>();
    static Map<String,Map> day1Result=new LinkedHashMap<>();

    //day2데이터
    static Map<String,String> day2_00=new HashMap<>();
    static Map<String,String> day2_03=new HashMap<>();
    static Map<String,String> day2_06=new HashMap<>();
    static Map<String,String> day2_09=new HashMap<>();
    static Map<String,String> day2_12=new HashMap<>();
    static Map<String,String> day2_15=new HashMap<>();
    static Map<String,String> day2_18=new HashMap<>();
    static Map<String,String> day2_21=new HashMap<>();
    static Map<String,Map> day2Result=new LinkedHashMap<>();


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
    static Map<String,String> per_03=new HashMap<>();
    static Map<String,String> per_06=new HashMap<>();
    static Map<String,String> per_09=new HashMap<>();
    static Map<String,String> per_12=new HashMap<>();

    static Map<String,String> per_15=new HashMap<>();
    static Map<String,String> per_18=new HashMap<>();
    static Map<String,String> per_21=new HashMap<>();


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

    //전체 시간 데이터


    //합친 코드 확인용 결과값
    static Map<String,String> concat=new LinkedHashMap<>();

    // 하루용 데이터 총 개수 14개만 넘기기 위한 변수
    int count;

    //시간 데이터 용
    Calendar cal;
    SimpleDateFormat sdf;

    String todayStr;
    Integer todayInteger;

    String tomorrowStr;
    Integer tomorrowInteger;

    String afterTomorrowStr;
    Integer afterTomorrowInteger;

    //open api사용 초단기 예보
    //초 단기 예보에서는 시간별 온도
    //sky,pty,t1h 만 파싱해 가져오기
    //매시간 30분 단위로 조절하기
//    @ResponseBody
//    @GetMapping("/todayWeather")
//    public Map weather() throws IOException, ParseException {
    public void weather() throws IOException, ParseException {

        //시간을 받아오는 코드
        //조회하는 시간에서 +1 정보만 가져온다.
        Integer currentTime= LocalDateTime.now().getHour();
        Integer min=LocalDateTime.now().getMinute();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = "60";	//위도
        String ny = "125";	//경도
        String baseTime=null;

        //분에 따른 baseTime 조절
        if(min<=30){
            Integer calTime;
            if(currentTime==0){
                calTime=23;
            }
            else {
                calTime=currentTime-1;
            }
            if(calTime<10){
                Integer.toString(calTime);
                String before30Minute="0"+calTime+"30";
                baseTime = before30Minute;	//조회하고싶은 시간
            }
            else{
                Integer.toString(calTime);
                String before30Minute=calTime+"30";
                baseTime = before30Minute;	//조회하고싶은 시간
            }


        }
        else
        {
            if(currentTime<10){
                Integer.toString(currentTime);
                String before30Minute="0"+currentTime+"30";
                baseTime = before30Minute;	//조회하고싶은 시간

            }
            else{
                String after30Minute=Integer.toString(currentTime)+"30";
                baseTime = after30Minute;	//조회하고싶은 시간
            }
        }
        System.out.println("baseTime = " + baseTime);
        String baseDate = todayStr;	//조회하고싶은 날짜
        String dataType = "json";	//타입 xml, json 등등 ..
        String numOfRows = "50";	//한 페이지 결과 수

        //전날 23시 부터 153개의 데이터를 조회하면 오늘과 내일의 날씨를 알 수 있음

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));	/* 타입 */
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));	/* 한 페이지 결과 수 */

        // GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        String data= sb.toString();

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
        todayCalHour(baseTime);
        System.out.println("parse_body = " + parse_body);
        for(int i=0;i<parse_item.size();i++){
            element =(JSONObject) parse_item.get(i);

            //시간 별 날씨 데이터 받아오기 함수 호출
            today(element,"0000",clock_00,"0시");
            today(element,"0100",clock_01,"1시");
            today(element,"0200",clock_02,"2시");
            today(element,"0300",clock_03,"3시");
            today(element,"0400",clock_04,"4시");
            today(element,"0500",clock_05,"5시");
            today(element,"0600",clock_06,"6시");
            today(element,"0700",clock_07,"7시");
            today(element,"0800",clock_08,"8시");
            today(element,"0900",clock_09,"9시");
            today(element,"1000",clock_10,"10시");
            today(element,"1100",clock_11,"11시");
            today(element,"1200",clock_12,"12시");

            today(element,"1300",clock_13,"13시");
            today(element,"1400",clock_14,"14시");
            today(element,"1500",clock_15,"15시");
            today(element,"1600",clock_16,"16시");
            today(element,"1700",clock_17,"17시");
            today(element,"1800",clock_18,"18시");
            today(element,"1900",clock_19,"19시");
            today(element,"2000",clock_20,"20시");
            today(element,"2100",clock_21,"21시");
            today(element,"2200",clock_22,"22시");
            today(element,"2300",clock_23,"23시");
            today(element,"2400",clock_00,"24시");



        }

//        return todayWeahterResult;
    }

    //하늘상태, 강수확률,강수형, 최고 ,최고 기온
    //SKY,POP,PTY
    //getMapping 네이밍 생각해보기
    //3시간 마다 날씨 정보 제공
//    @ResponseBody
//    @GetMapping("/per3today")
    public void per3today() throws IOException, ParseException {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";    //동네예보조회

        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = "60";    //위도
        String ny = "127";    //경도

        //시간을 받아오는 코드
        int currentTime= LocalDateTime.now().getHour();
        int min=LocalDateTime.now().getMinute();

        String time=Integer.toString(currentTime)+Integer.toString(min);

        String baseDate = "20210308";	//조회하고싶은 날짜
        String baseTime = "2000";    //API 제공 시간
        String dataType = "json";    //타입 xml, json
        String numOfRows = "255";    //한 페이지 결과 수
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
        JSONObject element=null;


        /**
         * 시간 관련
         */

        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        if(currentTime<6){
            cal.add(Calendar.DATE,-1);
        }
        String todayDate=sdf.format(cal.getTime());
        System.out.println("str = " + todayDate);

        //시각을 얻는 코드
        int t= LocalDateTime.now().getHour();
        System.out.println("currentTime = " + t);

        //1시간 단위와 동일한 시간 값 가져오기
        Integer valueMin=LocalDateTime.now().getMinute();
        String value=null;
        Integer calTime=null;



        //분에 따른 baseTime 조절
        if(valueMin<=30){

            if(t==0){
                calTime=23;
            }
            else {
                calTime=t-1;
            }
            if(calTime<10){
                Integer.toString(calTime);
                String before30Minute="0"+calTime+"30";
                value = before30Minute;	//조회하고싶은 시간
                System.out.println("##########");
            }
            else{
                Integer.toString(calTime);
                String before30Minute=calTime+"30";
                value = before30Minute;	//조회하고싶은 시간

            }

        }
        else
        {
            if(t<10){
                Integer.toString(t);
                String before30Minute="0"+t+"30";
                value = before30Minute;	//조회하고싶은 시간
                System.out.println("value = " + value);

            }
            else{
                String after30Minute=Integer.toString(currentTime)+"30";
                value = after30Minute;	//조회하고싶은 시간
            }

        }
        System.out.println("value = " + value);
        String cp=todayCalHour(value);
        System.out.println("cmp = " + cp);


        //시간 비교 위해 시간 데이터 변형
        // 0000~0900 fcstTime 용 데이터 변형
        String getTime=null;
        if(t>=0&&t<10){
           getTime= "0"+t+"00";
        }
        else{
            getTime=t+"00";
        }
        System.out.println("getTime = " + getTime);



        int cmp=155*11;
        System.out.println("parse_item = " + parse_item.size());
        System.out.println(data);
        for (int i = 0; i < parse_item.size(); i++) {
            if(count<14){


                object=(JSONObject) parse_item.get(i);
                todayPer03(object,"0000",per_00,"00시",cp);
                todayPer03(object,"0300",per_03,"3시",cp);
                todayPer03(object,"0600",per_06,"6시",cp);
                todayPer03(object,"0900",per_09,"9시",cp);
                todayPer03(object,"1200",per_12,"12시",cp);
                todayPer03(object,"1500",per_15,"15시",cp);
                todayPer03(object,"1800",per_18,"18시",cp);
                todayPer03(object,"2100",per_21,"21시",cp);






            }




        }

//        return todayWeahterResult;
    }



    void todayPer03(JSONObject object,String clock,Map clockValue,String clockName,String clockCmp){
//        System.out.println("clockCmp = " + clockCmp);
//        System.out.println(Integer.parseInt(clockCmp)<Integer.parseInt(clock));
        //오늘 일때
        if(object.get("fcstDate").equals(todayStr)) {
            if (Integer.parseInt(clockCmp) < Integer.parseInt(clock)) {

                if (object.get("fcstTime").equals(clock)) {
                    if (object.get("category").equals("SKY")) {
                        System.out.println("object = " + object);
                        count++;
                        System.out.println("count = " + count);
                        String skyValue = object.get("fcstValue").toString();
                        clockValue.put("SKY", skyValue);
                    } else if (object.get("category").equals("PTY")) {
                        String ptyValue = object.get("fcstValue").toString();
                        clockValue.put("PTY", ptyValue);
                    } else if (object.get("category").equals("T3H")) {
                        String T1H = object.get("fcstValue").toString();
                        clockValue.put("T3H", T1H);
                    }
                    todayWeahterResult.put(clockName, clockValue);
                }
            }
        }

                else if(object.get("fcstDate").equals(tomorrowStr)){
                    if(object.get("fcstTime").equals(clock)){
                        if(object.get("category").equals("SKY")){
                            System.out.println("tomorrowStr = " + tomorrowStr);
                            count++;
                            System.out.println("count = " + count);
                            String skyValue= object.get("fcstValue").toString();
                            clockValue.put("SKY",skyValue);
                        }
                        else if(object.get("category").equals("PTY")){
                            String ptyValue= object.get("fcstValue").toString();
                            clockValue.put("PTY",ptyValue);
                        }
                        else if(object.get("category").equals("T3H")){
                            String T1H= object.get("fcstValue").toString();
                            clockValue.put("T3H",T1H);
                        }
                        day1Result.put(clockName, clockValue);
                    }


                }

        else if(object.get("fcstDate").equals(afterTomorrowStr)){
            if(object.get("fcstTime").equals(clock)){
                if(object.get("category").equals("SKY")){
                    count++;
                    System.out.println("count = " + count);
                    String skyValue= object.get("fcstValue").toString();
                    clockValue.put("SKY",skyValue);
                }
                else if(object.get("category").equals("PTY")){
                    String ptyValue= object.get("fcstValue").toString();
                    clockValue.put("PTY",ptyValue);
                }
                else if(object.get("category").equals("T3H")){
                    String T1H= object.get("fcstValue").toString();
                    clockValue.put("T3H",T1H);
                }
                day2Result.put(clockName, clockValue);
            }

        }




    }


    public  void test(){

        cal=Calendar.getInstance();
        cal.setTime(new Date());
        sdf = new SimpleDateFormat("yyyyMMdd");
        todayStr=sdf.format(cal.getTime());
        todayInteger=Integer.parseInt(todayStr);
//        System.out.println("today = " + todayStr);

        cal.add(Calendar.DATE,+1);
        tomorrowStr=sdf.format(cal.getTime());
        tomorrowInteger=Integer.parseInt(tomorrowStr);
//        System.out.println("tomorrow = " + tomorrow);

        cal.add(Calendar.DATE,+1);
        afterTomorrowStr=sdf.format(cal.getTime());
        afterTomorrowInteger=Integer.parseInt(afterTomorrowStr);
//        System.out.println("afterTomorrow = " + afterTomorrow);

//        System.out.println("todayInteger + tomorrowInteger + afterTomorrowInteger = " + todayInteger + tomorrowInteger + afterTomorrowInteger);
    }



    //1시간 단위로 받아올 수 있는 최대 시간을 계산해 반환->3시간 단위로 받을 시각 계산위
    String todayCalHour(String baseTime){
        String returnTimeValue=null;
        Integer beforeCalValue=Integer.parseInt(baseTime);
        //6시간 더해 반환
        if(baseTime.equals("0030")||baseTime.equals("0330")||baseTime.equals("0630")||baseTime.equals("0930")||baseTime.equals("1230")||baseTime.equals("1530")||baseTime.equals("1830")||baseTime.equals("2130")){

            Integer afterCalValue=beforeCalValue+600;
            System.out.println("afterCalValue = " + afterCalValue);
            if(afterCalValue>2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            else if(afterCalValue==2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="00"+Integer.toString(afterCalValue);
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

            Integer afterCalValue=beforeCalValue+500;
            if(afterCalValue>=2430){
                afterCalValue=afterCalValue-2400;
                returnTimeValue="0"+afterCalValue;
            }
            else if(afterCalValue<1030&&afterCalValue>0030){

                returnTimeValue="0"+Integer.toString(afterCalValue);
            }
            else{
                returnTimeValue=Integer.toString(afterCalValue);

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



    //오늘의 날씨 1시간 단위로 불러오는 함수
    void today(JSONObject object,String clock,Map clockValue,String clockName){

        if(object.get("fcstTime").equals(clock)){

                if(object.get("category").equals("SKY")){
                    String skyValue= object.get("fcstValue").toString();
                    clockValue.put("SKY",skyValue);
                   // 총 날씨정보 제공 값 조절하기 위한 변수
                    count++;
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
//                concat.put(clockName,clockName);//코드 합쳐보기
        }



    }

    //하늘상태, 강수확률,강수형, 최고 ,최고 기온
    //SKY,POP,PTY,TMN,TMX
    //getMapping 네이밍 생각해보기
    //3~7일 데이터만 받기
    //6시 8
    //하루 전 데이터만 보관 오전 6시가 넘으면 해당 6시 조회
    @ResponseBody
    @GetMapping("/weeklyWeather")
    public String weather2() throws IOException, ParseException {

        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //시각을 얻는 코드
        int currentTime= LocalDateTime.now().getHour();
        System.out.println("currentTime = " + currentTime);

        if(currentTime<6){
          cal.add(Calendar.DATE,-1);
        }


        //날짜 값 빼기 연산
//        cal.add(Calendar.DATE,-1);

        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";	//중기기온조회

        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String regId = "11B10101";	//예보 구역 코드

        //발표시각 0600 1800시

        String tmFc = sdf.format(cal.getTime())+"0600";	//발표시각 입력
        String dataType = "json";	//타입 xml, json
        String numOfRows = "250";	//한 페이지 결과 수

        System.out.println("tmFc = " + tmFc);

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));	/* 타입 */

        // GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        String data= sb.toString();

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

        System.out.println(data);
        System.out.println(parse_item);

        return  "OK";
    }

    //시간 변경 확인용 시간 테스트 코드
    @ResponseBody
    @GetMapping("/time")
    public String time(){

        int currentTime= LocalDateTime.now().getHour();
        int min=LocalDateTime.now().getMinute();
        int year=LocalDateTime.now().getYear();
        int month=LocalDateTime.now().getMonthValue();

        String time=Integer.toString(currentTime)+Integer.toString(min);
        String day=Integer.toString(year)+Integer.toString(month);

        return day;
    }

    //사용자 위치 값 nx,ny로 변경

    @ResponseBody
    @GetMapping("/location")
    public void location() throws IOException, ParseException {

//        String test="금천구";
        String result;
        String areaTop="서울특별시";	//지역
        String areaMdl="금천구";
//        String areaLeaf="종로1가동";
        String code="";	//지역 코드
        String x;
        String y;



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


    }

    @ResponseBody
    @GetMapping("/concat")
    public Map concat() throws IOException, ParseException {

        //오늘 날짜 받아오기
        test();

        weather();
        per3today();

        ArrayList<Map>map =new ArrayList<>();
        Map<String,Map>hashMap1=new LinkedHashMap();
        Map<String,Map>hashMap2=new LinkedHashMap();
        Map<String,Map>hashMap3=new LinkedHashMap();
        hashMap1.put("오늘",todayWeahterResult);
        hashMap2.put("내일",day1Result);
        hashMap1.putAll(hashMap2);
        if(!day2Result.isEmpty()){
            hashMap3.put("모레",day2Result);
            hashMap1.putAll(hashMap3);
        }

        return hashMap1;




    }



}
