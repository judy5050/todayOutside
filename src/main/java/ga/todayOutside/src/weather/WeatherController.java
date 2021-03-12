package ga.todayOutside.src.weather;

import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;


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
    @GetMapping("/todayWeatherNow")
    public BaseResponse<Map> todayWeatherNow() throws IOException, ParseException {
        Map<String,String> nowWeatherResult=weatherService.getTodayWeatherNow();


        return new BaseResponse<>(BaseResponseStatus.SUCCESS,nowWeatherResult); //TODO:성공 코드 바꾸기

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

}
