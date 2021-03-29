package ga.todayOutside.src.disaster;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.disaster.model.DisasterAlarm;
import ga.todayOutside.src.disaster.model.DisasterFilterRes;
import ga.todayOutside.src.disaster.model.DisasterHomeInfoRes;
import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.user.UserInfoProvider;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DisasterService {

    private final DisasterProvider disasterProvider;
    private final DisasterRepository disasterRepository;
    private final DisasterAlarmRepository disasterAlarmRepository;
    private final JwtService jwtService;

    @Autowired
    public DisasterService(DisasterProvider disasterProvider, DisasterRepository disasterRepository, DisasterAlarmRepository disasterAlarmRepository, JwtService jwtService, UserInfoProvider userInfoProvider) {
        this.disasterProvider = disasterProvider;
        this.disasterRepository = disasterRepository;
        this.disasterAlarmRepository = disasterAlarmRepository;
        this.jwtService = jwtService;
        this.userInfoProvider = userInfoProvider;
    }

    private UserInfoProvider userInfoProvider;


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
        return result;
    }

    /**
     * DB 등록
     * @param messages
     */
    public ArrayList<DisasterInfo> postMsg(JSONArray messages) {
        //모델 매핑
        ArrayList<DisasterInfo> disasterInfos = disasterProvider.makeModel(messages);
        //DB 등록
        return disasterProvider.postMsg(disasterInfos);
    }

    /**
    재난 필터 로직
     **/
    public JSONObject filter(ArrayList<DisasterInfo> disasterInfos, String city, String state, Long userIdx) throws BaseException{

        JSONObject resultState = new JSONObject();
        JSONObject resultDisaster = new JSONObject();
        JSONObject result = new JSONObject();
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userIdx);

        Map<String, ArrayList<DisasterInfo>> stateFilter = null;

        if (userInfo == null) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

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
                    Map<String, ArrayList<DisasterFilterRes>> disasterFilter = filterByDisaster(cityFilter.get(cityKey), userIdx);

                    resultDisaster = disasterProvider.MapToJSON(disasterFilter);

                    return resultDisaster;
                    //resultCity.put(cityKey, resultDisaster);
                }


            }
        }
        resultState.put("calamity", null);
        resultState.put("total", 0);

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

            //유저 동, 구 아니면 건너뛰기
            if (!userCity.equals(city) && !city.equals("전체")) continue;

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
    public Map<String, ArrayList<DisasterFilterRes>> filterByDisaster(ArrayList<DisasterInfo> disasterInfos, Long userIdx) {

        DisasterAlarm disasterAlarm = disasterAlarmRepository.findByUserIdx(userIdx).orElse(null);

        Map<String, ArrayList<DisasterFilterRes>> result = new HashMap<>();
        Set<String> filter = disasterProvider.filterDisaster(disasterAlarm);

        for (DisasterInfo o : disasterInfos) {

            String kind = o.getKind();

            //알람 등록 안해놓은 재난은 건너뛰기
            if (!filter.contains(kind)) continue;

            DisasterFilterRes res = new DisasterFilterRes(o.getState(), o.getCity(), o.getMsg(), o.getCreateDate(), o.getMsgIdx(), kind);

            ArrayList<DisasterFilterRes> infos = result.getOrDefault("calamity", new ArrayList<DisasterFilterRes>());
            infos.add(res);
            result.put("calamity", infos);
        }

        if (result.size() == 0 ) result.put("calamity", null);

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

    /**
     * home 재난정보 조회
     */

    public DisasterHomeInfoRes getHomeInfo(ArrayList<DisasterInfo> todayDisaster) throws BaseException {

        Long userId = jwtService.getUserId();


        return new DisasterHomeInfoRes(123, "kind");
    }

}
