package ga.todayOutside.src.weather;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ga.todayOutside.config.secret.Secret;
import jdk.vm.ci.meta.MemoryAccessProvider;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.security.web.authentication.logout.DelegatingLogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.event.CellEditorListener;


@RestController
public class WeatherTest {


    //open api사용 초단기 예보
    //초 단기 예보에서는 시간별 온도
    //sky,pty,t1h 만 파싱해 가져오기
    @ResponseBody
    @GetMapping("/todayWeather")
    public Map weather() throws IOException, ParseException {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = "60";	//위도
        String ny = "125";	//경도
        String baseDate = "20210226";	//조회하고싶은 날짜
        String baseTime = "0000";	//조회하고싶은 시간
        String dataType = "json";	//타입 xml, json 등등 ..
        String numOfRows = "10000";	//한 페이지 결과 수

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

//        System.out.println("기본 데이터 출력 : "+data);
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

        JSONObject element;





        //JSONObject item = (JSONObject) parse_item.get("item");

        System.out.println(data);
//        for(int i=0;i<parse_item.size();i++) {
//            System.out.println(parse_item.get(i));
////            System.out.println("i = " + i);
//        }

        Map<String, Map> result=new HashMap<>();
        Map<String,String> clock_24=new HashMap<>();
        Map<String,String> clock_01=new HashMap<>();
        Map<String,String>clock_02=new HashMap<>();
        Map<String,String> clock_03=new HashMap<>();
        Map<String,String> clock_04=new HashMap<>();
        Map<String,String> clock_05=new HashMap<>();
        Map<String,String> clock_06=new HashMap<>();
        Map<String,String> clock_07=new HashMap<>();
        Map<String,String> clock_08=new HashMap<>();

        Map<String,String> clock_09=new HashMap<>();
        Map<String,String>clock_10=new HashMap<>();
        Map<String,String> clock_11=new HashMap<>();
        Map<String,String> clock_12=new HashMap<>();
        Map<String,String> clock_13=new HashMap<>();
        Map<String,String> clock_14=new HashMap<>();
        Map<String,String> clock_15=new HashMap<>();
        Map<String,String> clock_16=new HashMap<>();
        Map<String,String> clock_17=new ManagedMap<>();

        Map<String,String> clock_18=new HashMap<>();
        Map<String,String>clock_19=new HashMap<>();
        Map<String,String> clock_20=new HashMap<>();
        Map<String,String> clock_21=new HashMap<>();
        Map<String,String> clock_22=new HashMap<>();
        Map<String,String> clock_23=new HashMap<>();


        for(int i=0;i<parse_item.size();i++){
              element =(JSONObject) parse_item.get(i);

              //밤 12 시
              if(element.get("fcstTime").equals("0000")){
                      if(element.get("category").equals("SKY")){
//                          System.out.println("SKY = " + element);
                          String skyValue= element.get("fcstValue").toString();
                          clock_24.put("SKY",skyValue);
                      }
                      else if(element.get("category").equals("PTY")){
//                          System.out.println("PTY = " + element);
                          String ptyValue= element.get("fcstValue").toString();
                          clock_24.put("PTY",ptyValue);
                      }
                      else if(element.get("category").equals("T1H")){
//                          System.out.println("T1H = " + element);
                          String T1H= element.get("fcstValue").toString();
                          clock_24.put("T1H",T1H);
                      }


                      result.put("24시",clock_24);

              }


            //새벽 1 시
            if(element.get("fcstTime").equals("0100")){
                if(element.get("category").equals("SKY")){
//                    System.out.println("SKY = " + element);
                    String skyValue= element.get("fcstValue").toString();
                    clock_01.put("SKY",skyValue);
                }
                else if(element.get("category").equals("PTY")){
//                    System.out.println("PTY = " + element);
                    String ptyValue= element.get("fcstValue").toString();
                    clock_01.put("PTY",ptyValue);
                }
                else if(element.get("category").equals("T1H")){
//                    System.out.println("T1H = " + element);
                    String T1H= element.get("fcstValue").toString();
                    clock_01.put("T1H",T1H);
                }


                result.put("01시",clock_01);

            }

            //새벽 2 시
            if(element.get("fcstTime").equals("0200")){
                if(element.get("category").equals("SKY")){
//                    System.out.println("SKY = " + element);
                    String skyValue= element.get("fcstValue").toString();
                    clock_02.put("SKY",skyValue);
                }
                else if(element.get("category").equals("PTY")){
//                    System.out.println("PTY = " + element);
                    String ptyValue= element.get("fcstValue").toString();
                    clock_02.put("PTY",ptyValue);
                }
                else if(element.get("category").equals("T1H")){
//                    System.out.println("T1H = " + element);
                    String T1H= element.get("fcstValue").toString();
                    clock_02.put("T1H",T1H);
                }


                result.put("02시",clock_02);

            }

            //새벽 3 시
            if(element.get("fcstTime").equals("0300")){
                if(element.get("category").equals("SKY")){
//                    System.out.println("SKY = " + element);
                    String skyValue= element.get("fcstValue").toString();
                    clock_03.put("SKY",skyValue);
                }
                else if(element.get("category").equals("PTY")){
//                    System.out.println("PTY = " + element);
                    String ptyValue= element.get("fcstValue").toString();
                    clock_03.put("PTY",ptyValue);
                }
                else if(element.get("category").equals("T1H")){
//                    System.out.println("T1H = " + element);
                    String T1H= element.get("fcstValue").toString();
                    clock_03.put("T1H",T1H);
                }


                result.put("03시",clock_03);

            }


            //새벽 4 시
            if(element.get("fcstTime").equals("0400")){
                if(element.get("category").equals("SKY")){
//                    System.out.println("SKY = " + element);
                    String skyValue= element.get("fcstValue").toString();
                    clock_04.put("SKY",skyValue);
                }
                else if(element.get("category").equals("PTY")){
//                    System.out.println("PTY = " + element);
                    String ptyValue= element.get("fcstValue").toString();
                    clock_04.put("PTY",ptyValue);
                }
                else if(element.get("category").equals("T1H")){
//                    System.out.println("T1H = " + element);
                    String T1H= element.get("fcstValue").toString();
                    clock_04.put("T1H",T1H);
                }


                result.put("04시",clock_04);

            }

            //새벽 5 시
            if(element.get("fcstTime").equals("0500")){
                if(element.get("category").equals("SKY")){
//                    System.out.println("SKY = " + element);
                    String skyValue= element.get("fcstValue").toString();
                    clock_05.put("SKY",skyValue);
                }
                else if(element.get("category").equals("PTY")){
//                    System.out.println("PTY = " + element);
                    String ptyValue= element.get("fcstValue").toString();
                    clock_05.put("PTY",ptyValue);
                }
                else if(element.get("category").equals("T1H")){
//                    System.out.println("T1H = " + element);
                    String T1H= element.get("fcstValue").toString();
                    clock_05.put("T1H",T1H);
                }


                result.put("05시",clock_05);

            }
//            System.out.println("element = " + element.get("category"));
//            System.out.println("element = " + element);
//            Set set = element.entrySet();
//            for (Object test : set) {
//                System.out.println(test.toString());
//
//            }
//            Collection<String> keys=element.values();

        }






        return result;
    }



    //하늘상태, 강수확률,강수형, 최고 ,최고 기온
    //SKY,POP,PTY,TMN,TMX
    //getMapping 네이밍 생각해보기
    @ResponseBody
    @GetMapping("/weeklyWeather")
    public String weather2() throws IOException, ParseException {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";    //동네예보조회

        // 홈페이지에서 받은 키
        String serviceKey = Secret.WEATHER_OPEN_APIKEY;
        String nx = "60";    //위도
        String ny = "127";    //경도


        String baseDate = "20210225";	//조회하고싶은 날짜
        String baseTime = "2300";    //API 제공 시간
        String dataType = "json";    //타입 xml, json
        String numOfRows = "255";    //한 페이지 결과 수

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

          int Info;//구름 ,맑음 정보 값 임의 네이밍 ->나중에 수정

//        System.out.println(data);
        for (int i = 0; i < parse_item.size(); i++) {
            object=(JSONObject) parse_item.get(i);
//            System.out.println("parse_item = " + parse_item);

            //SKY,POP,PTY,TMN,TMX
            //sky =3 구름 많움 하늘 상태
            if(object.get("category").equals("SKY")){
                System.out.println("object = " + object);
                Stream stream = object.values().stream();
                Object fcstValue = object.get("fcstValue");
                System.out.println("SKY = " + fcstValue);
            }
            //강수 확률 POP
            //항상 fcstime 은 pop부터 변경
             else if(object.get("category").equals("POP")){
                System.out.println("object = " + object);
                Stream stream = object.values().stream();
                Object fcstValue = object.get("fcstValue");
                System.out.println("POP = " + fcstValue);
            }
            //강수형태 PTY
            else if(object.get("category").equals("PTY")){
                System.out.println("object = " + object);

                Object fcstValue = object.get("fcstValue");
                System.out.println("PTY = " + fcstValue);
            }

            //최저기온
            else if(object.get("category").equals("TMN")){
                System.out.println("object = " + object);
                Stream stream = object.values().stream();

                Object fcstValue = object.get("fcstValue");
                System.out.println("TMN = " + fcstValue);
            }

            else if(object.get("category").equals("TMX")){
                System.out.println("object = " + object);

                Object fcstValue = object.get("fcstValue");
                System.out.println("TMX = " + fcstValue.getClass());
            }

//            System.out.println(parse_item.get(i));
        }

        return "OK";
    }

    //시간 변경 확인용 시간 테스트 코드
    @ResponseBody
    @GetMapping("/time")
    public String time(){

        int currentTime= LocalDateTime.now().getHour();
        int min=LocalDateTime.now().getMinute();

        String time=Integer.toString(currentTime)+Integer.toString(min);


        return time;
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







}
