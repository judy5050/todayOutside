package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    /**
    재난 필터 로직
     **/
    public JSONObject filter(JSONArray messages) {

        JSONObject resultState = new JSONObject();

        JSONObject resultDisaster = new JSONObject();

        Map<String, ArrayList<DisasterInfo>> stateFilter = null;

        //도 필터
        ArrayList<DisasterInfo> disasterInfos = disasterProvider.makeModel(messages);
        stateFilter = filterByState(disasterInfos);

        if (disasterInfos == null) {
            return null;
        }

        else {
            for (String key : stateFilter.keySet()) {
                //시 필터
                Map<String, ArrayList<DisasterInfo>> cityFilter = filterByCity(stateFilter.get(key));
                JSONObject resultCity = new JSONObject();


                for (String cityKey : cityFilter.keySet()) {

                    //재난 필터
                    Map<String, ArrayList<DisasterInfo>> disasterFilter = filterByDisaster(cityFilter.get(cityKey));
                    resultDisaster = disasterProvider.MapToJSON(disasterFilter);

                    resultCity.put(cityKey, resultDisaster);

                }

                resultState.put(key, resultCity);

            }
        }

        return resultState;
    }

    /**
     * 도 단위의 필터
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

        return result;
    }

    /**
     * 시 단위의 필터
     * @return
     */
    public Map<String, ArrayList<DisasterInfo>> filterByCity(ArrayList<DisasterInfo> disasterInfos) {
        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();

        /*
        시 필터링
         */
        for (DisasterInfo o : disasterInfos) {

            String city = o.getCity();

            ArrayList<DisasterInfo> infos = result.getOrDefault(city, new ArrayList<DisasterInfo>());
            infos.add(o);
            result.put(city, infos);

        }

        return result;

    }

    /**
     * 재난 필터링
     * @param disasterInfos
     * @return
     */
    public Map<String, ArrayList<DisasterInfo>> filterByDisaster(ArrayList<DisasterInfo> disasterInfos) {

        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();

        for (DisasterInfo o : disasterInfos) {

            String msg = o.getMsg();
            String disaster = disasterProvider.findKeyword(msg);

            ArrayList<DisasterInfo> infos = result.getOrDefault(disaster, new ArrayList<DisasterInfo>());
            infos.add(o);
            result.put(disaster, infos);
        }

        return result;
    }

}
