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


    @ResponseBody
    @GetMapping("/todayWeathers")
    public BaseResponse<Map> todayWeathers() throws IOException, ParseException {

        Map result =weatherService.getTodayWeatherList();



        return new BaseResponse<>(BaseResponseStatus.SUCCESS,result);
    }

}
