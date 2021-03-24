package ga.todayOutside.src.weather;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.user.UserInfoService;
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
    private final UserInfoService userInfoService;

    /**
     *오늘 날씨 조회 리스트
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/time-weathers")
    public BaseResponse<Map> todayWeatherList(@PathVariable Long addressIdx) throws IOException, ParseException {

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
        Map<String, String> nxNy = weatherService.convertNxNy(address.getFirstAddressName(), address.getSecondAddressName());

        //x,y 값 얻기
        String nx=nxNy.get("x");
        String ny=nxNy.get("y");

        //nx, ny,userIdx 확인
        System.out.println("nx = " + nx);
        System.out.println("ny = " + ny);
        System.out.println("userIdx :"+userIdx);
        Map res;
        res =weatherService.getTodayWeatherList(nx,ny);



        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_TIME_WEATHER,res); //TODO: 성공 코드 바꾸기
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
        Map<String, String> nxNy = weatherService.convertNxNy(address.getFirstAddressName(), address.getSecondAddressName());

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
    @GetMapping("/address/{addressIdx}/today-hign-low")
    public BaseResponse<Map> todayWeatherHighAndLow(@PathVariable Long addressIdx) throws IOException, ParseException {


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
        Map<String, String> nxNy = weatherService.convertNxNy(address.getFirstAddressName(), address.getSecondAddressName());

        //x,y 값 얻기
        String nx=nxNy.get("x");
        String ny=nxNy.get("y");
        Map<String,String> todayWeatherHighAndLowResult=weatherService.getTodayWeatherHighAndLow(nx,ny);


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_TODAY_LOW_HIGH,todayWeatherHighAndLowResult);
    }

    /**
     *주간 날씨 데이터 조회
     * 최고 기온 및 최저 기온 조회
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/weekly-highAndLowWeathers")
    public BaseResponse<Map> weekly(@PathVariable  Long addressIdx) throws IOException, ParseException {

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
        Map<String, String> nxNy = weatherService.convertNxNy(address.getFirstAddressName(), address.getSecondAddressName());

        //x,y 값 얻기
        String nx=nxNy.get("x");
        String ny=nxNy.get("y");

        String s = weatherService.convertForWeeklyHighAndLowWeather(address.getFirstAddressName(),address.getSecondAddressName());

        weatherService.getDay1WeatherHighAndLow(nx,ny);
        Map result = weatherService.weeklyHighAndLow(s);

          return  new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_WEEKLY_HIGH_LOW_VALUE,result);//:TODO 성공 코드 바꾸기
    }

    /**
     *
     * @param addressIdx
     * @return
     * @throws IOException
     * @throws ParseException
     * 주간 강수확률 밑 날씨
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/weekly-weathers")
    public BaseResponse<Map> data(@PathVariable  Long addressIdx) throws IOException, ParseException {

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
        Map<String, String> nxNy = weatherService.convertNxNy(address.getFirstAddressName(), address.getSecondAddressName());

        //x,y 값 얻기
        String nx=nxNy.get("x");
        String ny=nxNy.get("y");

        //
        String s = weatherService.convertForWeeklyWeatherForeCast(address.getFirstAddressName(),address.getSecondAddressName());
        System.out.println("s = " + s);
        weatherService.getDay1Weather(nx,ny);
//        String s= weatherService.convertForWeeklyWeatherForeCast(getWeeklyReq.getSecondAddressName());
        Map weeklyForecastResult = weatherService.weeklyForecastResult(s);


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_WEEKLY_RAIN_WEATHER,weeklyForecastResult);
    }

    /**
     * 홈 화면용 날씨 데이터
     */
    @ResponseBody
    @GetMapping("/homeWeather")
    public BaseResponse<ArrayList> home(){

        Long userIdx;
        Address address;
        Map allAddressesByUserIdx=null;
        JSONArray allAddressesByUserIdx1;
        ArrayList allAddressesByUserIdx2=null;

        try {
            userIdx = jwtService.getUserId();
            userInfoService.findByUserIdx(userIdx);

             allAddressesByUserIdx2 = addressService.getAllAddressesByUserIdx(userIdx);



        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_HOME_WEATHER, allAddressesByUserIdx2);
    }




}
