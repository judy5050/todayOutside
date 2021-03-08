package ga.todayOutside.src.disaster;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class DisasterService {

    /**
     *  재난 정보 가져오기
     */
    public Map<String, Object> getImfomation() {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity<>(headers);
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            String url = "http://apis.data.go.kr/1741000/DisasterMsg2/getDisasterMsgList";
            String serviceKey = "BtXq5fRG2%2Bv%2B%2FEVKm3iuwIj%2BjPQnTRsO3yp6ZhtElvdMODumC6aSKDBkKtamNx9yp6YDVxes2fz5bK5FxZJI1Q%3D%3D";
            String pageNo = "1";
            String numOfRows = "10";
            String type = "json";
            String flag = "y";

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("ServiceKey", serviceKey)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("type", type)
                    .queryParam("flag", flag)
                    .build();
            System.out.println(builder.toString());

            ResponseEntity response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(response.toString());

            System.out.println(response);
        } catch (HttpClientErrorException | HttpServerErrorException | ParseException e) {
            return result;
        }
        return result;
    }

}
