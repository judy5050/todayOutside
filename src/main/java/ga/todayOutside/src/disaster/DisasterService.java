package ga.todayOutside.src.disaster;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

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
            String url = "http://apis.data.go.kr";
            //uricomponentbuilder를 내부적으로 사용해 uri 템플릿을 확장하고 인코딩
            DefaultUriBuilderFactory  factory = new DefaultUriBuilderFactory(url);

            RestTemplate restTemplate = new RestTemplateBuilder().build();
            factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

            String serviceKey = "BtXq5fRG2%2Bv%2B%2FEVKm3iuwIj%2BjPQnTRsO3yp6ZhtElvdMODumC6aSKDBkKtamNx9yp6YDVxes2fz5bK5FxZJI1Q%3D%3D";
            String pageNo = "1";
            String numOfRows = "3";
            String type = "json";
            String flag = "y";

            UriBuilder uriBuilder = factory.builder();

            uriBuilder.path("/1741000/DisasterMsg2/getDisasterMsgList")
                    .queryParam("ServiceKey", serviceKey)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("type", type)
                    .queryParam("flag", flag);


            ResponseEntity response = restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, null, String.class);

            result.put("body", response.getBody());
            result.put("status", response.getStatusCodeValue());


        } catch (HttpClientErrorException | HttpServerErrorException   e) {
            result.put("statusCode", e.getRawStatusCode());

        }
        System.out.println(result);
        return result;
    }

}
