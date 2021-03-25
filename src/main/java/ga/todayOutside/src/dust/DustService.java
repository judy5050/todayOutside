package ga.todayOutside.src.dust;


import ga.todayOutside.config.secret.Secret;
import lombok.Getter;
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
@Getter
public class DustService {

    Map<String,String> m=new HashMap<>();

    public Map getDust(String secondAddressName) throws IOException, ParseException {

        System.out.println("getDust");
        String stationName=" ";
        int index;
        //게시글 필터링 구 정보 받기
        if(secondAddressName.length()>=2&&secondAddressName.length()<=4){
            stationName=secondAddressName;
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            index=secondAddressName.indexOf("시");
            stationName=secondAddressName.substring(index+1);
            System.out.println("index = " + index);

        }

        String serviceKey = Secret.SERVICE;
        String dustKey=Secret.DUST_SERVICE_KEY;
        String dataType = "json";    //타입 xml, json 등등 ..
        String numOfRows="100";
        String pageNo="1";
        String dataTerm="DAILY";
        System.out.println("stationName = " + stationName);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode(dustKey, "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); /*xml 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("stationName","UTF-8") + "=" + URLEncoder.encode("종로구", "UTF-8")); /*측정소 이름*/
        urlBuilder.append("&" + URLEncoder.encode("dataTerm","UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8")); /*요청 데이터기간(1일: DAILY, 1개월: MONTH, 3개월: 3MONTH)*/
        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.3", "UTF-8")); /*버전별 상세 결과 참고*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
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
        String data =sb.toString();

        System.out.println("data = " + data);

        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data);

        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
//        System.out.println("parse_response = " + parse_response);
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
//        System.out.println("parse_body = " + parse_body);

        // body 로 부터 items 찾기
        JSONArray parse_items =(JSONArray)  parse_body.get("items");
//        System.out.println("parse_items = " + parse_items);

        //element 변수 선언
        JSONObject element=null;


        for (int i = 0; i < parse_items.size(); i++) {
            element = (JSONObject) parse_items.get(0);
//            System.out.println("element = " + element);
//            System.out.println("parse_items = " + parse_items.get(1));
            break;




        }
//        System.out.println("element.get(\"pm10Grade\") = " + element.get("pm10Grade"));
        String dustValue=element.get("pm10Grade1h").toString();
        String fineDust=element.get("pm25Grade1h").toString();
        m.put("fineDust",fineDust);
        m.put("dust", dustValue);
//        System.out.println(m);

        return m;


    }
}
