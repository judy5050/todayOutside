package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.sql.Timestamp;
import java.util.*;

@Service
public class DisasterService {

    @Autowired
    private DisasterProvider disasterProvider;
    @Autowired
    private DisasterRepository disasterRepository;
    @Autowired
    private DisasterAlarmRepository disasterAlarmRepository;
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
            String pageNo = "9";
            String numOfRows = "10";
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
     * DB 등록
     * @param messages
     */
    public void postMsg(JSONArray messages) {
        //모델 매핑
        ArrayList<DisasterInfo> disasterInfos = disasterProvider.makeModel(messages);
        //DB 등록
        disasterProvider.postMsg(disasterInfos);

        return;
    }

    /**
    재난 필터 로직
     **/
    public JSONObject filter(ArrayList<DisasterInfo> disasterInfos, String city, String state, Long userIdx) {

        JSONObject resultState = new JSONObject();
        JSONObject resultDisaster = new JSONObject();
        JSONObject result = new JSONObject();

        Map<String, ArrayList<DisasterInfo>> stateFilter = null;

        //도 필터
        stateFilter = filterByState(disasterInfos, state);

        if (disasterInfos == null) {
            return null;
        }

        else {
            for (String key : stateFilter.keySet()) {
                //시 필터
                Map<String, ArrayList<DisasterInfo>> cityFilter = filterByCity(stateFilter.get(key), city);
                JSONObject resultCity = new JSONObject();

                for (String cityKey : cityFilter.keySet()) {

                    //재난 필터
                    Map<String, ArrayList<DisasterInfo>> disasterFilter = filterByDisaster(cityFilter.get(cityKey), userIdx);
                    resultDisaster = disasterProvider.MapToJSON(disasterFilter);

                    return resultDisaster;
                    //resultCity.put(cityKey, resultDisaster);
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
    public Map<String, ArrayList<DisasterInfo>> filterByState(ArrayList<DisasterInfo> disasterInfos, String userState) {

        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();

        for (DisasterInfo o : disasterInfos) {

            String state = o.getState();
            //유저동네 아니면 건너뛰기
            if (!userState.equals(state)) continue;

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
    public Map<String, ArrayList<DisasterInfo>> filterByCity(ArrayList<DisasterInfo> disasterInfos, String userCity) {
        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();

        /*
        시 필터링
         */
        for (DisasterInfo o : disasterInfos) {

            String city = o.getCity();

            //유저 동 아니면 건너뛰기
            if (!userCity.equals(city)) continue;

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
    public Map<String, ArrayList<DisasterInfo>> filterByDisaster(ArrayList<DisasterInfo> disasterInfos, Long userIdx) {

        DisasterAlarm disasterAlarm = disasterAlarmRepository.findByUserIdx(userIdx);

        Map<String, ArrayList<DisasterInfo>> result = new HashMap<>();
        Set<String> filter = disasterProvider.filterDisaster(disasterAlarm);

        for (DisasterInfo o : disasterInfos) {

            String msg = o.getMsg();
            String disaster = disasterProvider.findKeyword(msg);
            //알람 등록 안해놓은 재난은 건너뛰기
            if (!filter.contains(disaster)) continue;

            ArrayList<DisasterInfo> infos = result.getOrDefault(disaster, new ArrayList<DisasterInfo>());
            infos.add(o);
            result.put(disaster, infos);
        }

        return result;
    }

    /**
     * 월 별 데이터 조회
     * @return
     */
    public ArrayList<DisasterInfo> filterByMonth(String month) throws BaseException {

        String s = "2021-"+ month +"-01 00:00:00";
        String e = "2021-"+ month +"-31 23:59:59";

        ArrayList<DisasterInfo> result = disasterRepository.findAllByCreateDateBetween(s, e);

        return result;
    }

    /**
     * 일별 데이터 조회
     * @param month
     * @param day
     * @return
     * @throws BaseException
     */
    public ArrayList<DisasterInfo> filterByDay(String month, String day) throws BaseException {

        String s = "2021-"+ month +"-"+ day +" 00:00:00";
        String e = "2021-"+ month +"-"+ day +" 23:59:59";

        ArrayList<DisasterInfo> result = disasterRepository.findAllByCreateDateBetween(s, e);

        return result;
    }

    public void postAlarm(List<String> name, Long userId) throws BaseException {

        try {
            DisasterAlarm disasterAlarm = disasterProvider.makeDisasterAlarm(name);
            disasterAlarm.setUserIdx(userId);
            System.out.println(userId);
            System.out.println(disasterAlarm);

            disasterAlarmRepository.save(disasterAlarm);

        } catch (Exception igored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_ALARAM);

        }
    }

}
