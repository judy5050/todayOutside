package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class DisasterService {

    @Autowired
    private DisasterProvider disasterProvider;
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
            String numOfRows = "30";
            String type = "static";
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

    /**
    재난 필터 로직
     **/
    public Map<String, Object> filter(JSONArray messages) {
        Map<String, Object> result = null;

        //도 단위로 필터
        ArrayList<DisasterInfo> disasterInfos = disasterProvider.makeModel(messages);
        filterByState(disasterInfos);
        filterByCity();
        //시 단위로 필터

        return result;
    }

    /**
     * 도 단위의 필터 -> 테스트 완료
     * @param disasterInfos
     * @return
     */
    public Map<String, ArrayList<DisasterInfo>> filterByState(ArrayList<DisasterInfo> disasterInfos) {
        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();

        for (DisasterInfo o : disasterInfos) {

            String state = o.getState();

            ArrayList<DisasterInfo> infos = result.getOrDefault(state, new ArrayList<DisasterInfo>());
            infos.add(o);
            result.put(state, infos);

        }

        //결과 테스트 로직 -> 테스트 성공
//        for (String key : result.keySet()) {
//            ArrayList<DisasterInfo> al = result.get(key);
//
//            for (DisasterInfo d : al)
//                System.out.println(d);
//        }

        return result;
    }

    /**
     * 시 단위의 필터
     * @return
     */
    public Map<String, Object> filterByCity() {
        Map<String, Object> result = null;
        return result;
    }

}
