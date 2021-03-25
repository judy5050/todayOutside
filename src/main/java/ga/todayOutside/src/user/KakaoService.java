package ga.todayOutside.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {
    /**
     *  토큰 검증
     */

    public Map<String, Object> accessToken(String accessToken) throws JsonProcessingException {
        //accesstoken 검증
        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString = "";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "https://kapi.kakao.com/v1/user/access_token_info";

            System.out.println(accessToken);

            header.add("Authorization","Bearer " + accessToken);
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("status", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e.toString());
            //e.toString()
            return result;
        } catch (Exception e) {
            result.put("statusCode","999");
            System.out.println(e.toString());

            return result;
        }
        //에러 관련 문서 -> 토큰 정보 보기 탭
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info


        return result;

    }

    /**
     *
     * 카카오 유저 정보 조회
     */
    public Map<String, Object> getUserInfo(String accessToken) throws BaseException {

        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString = "";

        //accesstoken 값 추출
        accessToken = accessToken.split("=")[1];

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "https://kapi.kakao.com/v2/user/me";

            header.add("Authorization", "Bearer " + accessToken);
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("status", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e.toString());
            throw new BaseException(BaseResponseStatus.INVALID_ACCESSTOKEN);

        } catch (Exception e) {
            result.put("statusCode", "999");
            System.out.println(e.toString());
        }
        //에러 관련 문서 -> 토큰 정보 보기 탭
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info

        return result;
    }
}
