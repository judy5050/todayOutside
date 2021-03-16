package ga.todayOutside.src.weather;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.weather.model.GetTodayNowRes;
import ga.todayOutside.src.weather.model.GetWeeklyReq;
import ga.todayOutside.src.weather.model.GetWeeklyRes;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private  final JwtService jwtService;
    private final AddressService addressService;

    /**
     *오늘 날씨 조회 리스트
     */
    @ResponseBody
    @GetMapping("/todayWeatherList")
    public BaseResponse<Map> todayWeatherList() throws IOException, ParseException {

        Map result =weatherService.getTodayWeatherList();



        return new BaseResponse<>(BaseResponseStatus.SUCCESS,result); //TODO: 성공 코드 바꾸기
    }

    /**
     * 현재 시각 날씨 조회
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/now-weather")
    public BaseResponse<Map> todayWeatherNow(@PathVariable Long addressIdx) throws IOException, ParseException {

        //jwt 토큰 에서 userIdx 얻기

        Long userIdx;
        Address address;

        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        //user idx 와 입력받은 addressIdx 일치 여부 확인 및 address 반환
        try {
             address = addressService.findByAddress(addressIdx, userIdx);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

        // 시,도 구 정보 받아 nx ny로 좌표 변경
        Map<String, String> nxNy = weatherService.converNxNy(address.getFirstAddressName(), address.getSecondAddressName());

        //x,y 값 얻기
        String nx=nxNy.get("x");
        String ny=nxNy.get("y");

        //nx, ny,userIdx 확인
        System.out.println("nx = " + nx);
        System.out.println("ny = " + ny);
        System.out.println("userIdx :"+userIdx);
        Map res;

        res = weatherService.getTodayWeatherNow(nx, ny);


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_NOW_WEATHER,res); //TODO:성공 코드 바꾸기

    }

    /**
     * 오늘의 최고 기온 및 최저기온
     */

    @ResponseBody
    @GetMapping("/todayWeatherHighAndLow")
    public BaseResponse<Map> todayWeatherHighAndLow() throws IOException, ParseException {
        Map<String,String> todayWeatherHighAndLowResult=weatherService.getTodayWeatherHighAndLow();


        return new BaseResponse<>(BaseResponseStatus.SUCCESS,todayWeatherHighAndLowResult);
    }

    /**
     *주간 날씨 데이터 조회
     */
    @ResponseBody
    @GetMapping("/weeklyHighAndLosWeather")
    public BaseResponse<Map> weekly(@RequestBody GetWeeklyReq getWeeklyReq) throws IOException, ParseException {

        String s = weatherService.convertForWeeklyHighAndLowWeather(getWeeklyReq.getSecondAddressName());
        Map result = weatherService.weeklyHighAndLow(s);

          return  new BaseResponse<>(BaseResponseStatus.SUCCESS,result);//:TODO 성공 코드 바꾸기
    }


    @ResponseBody
    @GetMapping("/weeklyForecast")
    public BaseResponse<Map> data(@RequestBody GetWeeklyReq getWeeklyReq) throws IOException, ParseException {
        String s= weatherService.convertForWeeklyWeatherForeCast(getWeeklyReq.getSecondAddressName());
        Map weeklyForecastResult = weatherService.weeklyForecastResult(s);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS,weeklyForecastResult);
    }

}
