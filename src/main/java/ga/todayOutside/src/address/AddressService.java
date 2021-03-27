package ga.todayOutside.src.address;



import com.fasterxml.jackson.databind.ser.Serializers;
import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.*;
import ga.todayOutside.src.dust.DustService;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.src.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {


    private final AddressRepository addressRepository;
    private final UserInfoService userInfoService;
    private final UserInfoRepository userInfoRepository;
    private final WeatherService weatherService;
    private final DustService dustService;
     Map<String,String> thirdAddressResult =new LinkedHashMap<>();
     JSONObject jsonObject=new JSONObject();
     JSONArray jsonArray=new JSONArray();
//    ArrayList<Map> weatherList=new ArrayList<>();
    /**
     *회원 주소 등록
     */
    @Transactional
    public Address createAddress(Long userIdx, PostAddressReq postAddressReq) throws BaseException{

        //회원 정보 가져와서 메인 주소지가 있는지 확인
        //있을경우 서브 주소로 추가
        //없을경우 메인 주소로 추가
        UserInfo userInfo;
        Address address=null;

        userInfo=userInfoService.findByUserIdx(userIdx);
        List<Address> addressList;
        addressList=addressRepository.findByUserAddress(userIdx);

        //addressList 가 저장된 것이 없는 경우 ->그 다음 저장 주소는 main
         if(addressList.size()==0){
             address=new Address(userInfo,postAddressReq.getFirstAddressName(), postAddressReq.getSecondAddressName(),1);
            System.out.println("address = " + address.getFirstAddressName());
            addressRepository.save(address);

        }

        //addressList 가 이미 저장된 것이 있는 경우 ->그 다음 저장 주소는 sub
        else if(addressList.size()==1){
            address=new Address(userInfo,postAddressReq.getFirstAddressName(), postAddressReq.getSecondAddressName(),2);
            addressRepository.save(address);

        }
        else if(addressList.size()>=2) {
            throw  new BaseException(BaseResponseStatus.FAILED_TO_POST_ADDRESS);
         }




        return address;


    }

    /**
     * 주소 삭제
     * @param addressIdx
     */
    @Transactional
    public void deleteAddress(Long addressIdx) throws BaseException, NoSuchElementException {
        Optional<Address> address;
        Address addressCheck; //네이밍 생각
        try {
                address=addressRepository.findById(addressIdx);
                addressCheck=address.get();
        }catch (NoSuchElementException exception){
            throw  new BaseException(BaseResponseStatus.NOT_FOUND_ADDRESS);
        }
            addressRepository.delete(addressCheck);
    }

    /**
     *주소 리스트 조회
     */
    public List<GetAddressRes> addressByUserIdx(Long userIdx) throws BaseException{

        List<GetAddressRes> address;

        UserInfo userInfo;
        try {
            userInfo=userInfoRepository.findById(userIdx).get();
            System.out.println("userInfo = " + userInfo.getNickname());
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        address=addressRepository.findAllByUserIdx(userIdx);
        if(address.isEmpty()){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);
        }



        return  address;
    }


    /**
     *회원이 갖고 있는 addressIdx 와 파라미터로 받은 addressIdx 가 일치하는지 확인
     */
    public  Address findByAddress(Long addressIdx,Long userIdx)throws BaseException{
        boolean check=false;
        UserInfo userInfo;
        Address address;

        try {
            userInfo=userInfoRepository.findById(userIdx).get();
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }


        try {
            List<Address> addressList = addressRepository.findByUserIdxAndAddressIdx(userIdx, addressIdx);
            for (Address address1 : addressList) {
                if(address1.getId().equals(addressIdx)){
                    check=true;
                }
            }
            if (check==false){
                throw  new BaseException(BaseResponseStatus.HAVE_NOT_ADDRESS);
            }
            address=addressRepository.findById(addressIdx).orElse(null);

            if(address==null){
                throw  new BaseException(BaseResponseStatus.NOT_FOUND_ADDRESS);
            }
        }catch (BaseException exception){
            throw new BaseException(exception.getStatus());
        }



        return address;


    }

    /**
     *주소 이름 수정
     */
    @Transactional
    public void patchAddressName(Address address,PatchAddressNameReq patchAddressNameReq){
        Address address1;
        address1 = addressRepository.findById(address.getId()).orElse(null);
        if(patchAddressNameReq.getFirstAddressName()!=null){
            address1.setFirstAddressName(patchAddressNameReq.getFirstAddressName());
        }
        if(patchAddressNameReq.getSecondAddressName()!=null){
            address1.setSecondAddressName(patchAddressNameReq.getSecondAddressName());
            address1.setThirdAddressName(null);
        }
        if(patchAddressNameReq.getThirdAddressName()!=null){
            System.out.println("thirdAddressName");
            address1.setThirdAddressName(patchAddressNameReq.getThirdAddressName());
        }

        addressRepository.save(address1);

    }

    /**
     * 주소 삭제 후 남은 address 의 addressOrder 수정하기
     */

    @Transactional
    public  void bulkAddressOrder(Long userIdx)throws BaseException{
        Address address;

         List<Address> addressList = addressRepository.findByUserIdx(userIdx);
            if(addressList.size()>=1){
                for(int i=0;i<addressList.size();i++){
                   address = addressList.get(i);
                   addressRepository.subBulkAddressOrder(address.getId());


                }
        }
    }

    @Transactional
    public void patchAddressOrder(Long userIdx, Long addressIdx, PatchAddressOrder patchAddressOrder,Integer maxAddress) {

        Address address;
        Address address1;

        address1 = addressRepository.findById(addressIdx).orElse(null);
        //순서 수정
        address1.setAddressOrder(patchAddressOrder.getAddressOrder());
        //update
        addressRepository.save(address1);


        List<Address> addressList = addressRepository.findByUserIdx(userIdx);
        if(addressList.size()>=1){
            for(int i=0;i<addressList.size();i++){
                address = addressList.get(i);
                //변경하고자 하는 addressOrder 값 보다 같거나 클 경우 addressOrder 을 +1씩 해준다.
                if(address.getId()!=addressIdx&&address.getAddressOrder()>= patchAddressOrder.getAddressOrder()&&address.getAddressOrder() <maxAddress)
                addressRepository.plusBulkAddressOrder(address.getId());
                else if(address.getId()!=addressIdx&&address.getAddressOrder()>= patchAddressOrder.getAddressOrder()&&address.getAddressOrder() >=maxAddress){
                    addressRepository.subBulkAddressOrder(address.getId());
                }

            }
        }


    }

    /**
     * 구 정보에 맞는 동네 목록 조회 API
     */
    public JSONArray getThirdAddressesName(String firstAddressName,String secondAddressName) throws IOException, ParseException {

        String result;
        String areaTop=firstAddressName;
        String areaMdl=secondAddressName;
        String code="";	//지역 코드

        URL url;
        BufferedReader br;
        URLConnection conn;

        JSONParser parser;
        JSONArray jArr;
        JSONObject jobj;

        //시 검색
        result=null;
        url = new URL("https://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            result = br.readLine().toString();
            br.close();
        }catch (NullPointerException e){
            System.out.println(e);
        }

        System.out.println(result);

        jArr=null;
        //시에 맞는 코드를 가져오는 코드
        parser = new JSONParser();
        try{
            jArr = (JSONArray) parser.parse(result);
        }catch (NullPointerException e){
            System.out.println("e = " + e);
        }


        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
            if(jobj.get("value").equals(areaTop)) {
                code=(String)jobj.get("code");
                System.out.println(areaTop+"코드 : "+code);
                break;
            }
        }

        //구 검색
        url = new URL("https://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+code+".json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = br.readLine().toString();
        br.close();
        //System.out.println(result);

        parser = new JSONParser();
        jArr = (JSONArray) parser.parse(result);

        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
            if(jobj.get("value").equals(areaMdl)) {
                code=(String)jobj.get("code");
                System.out.println("jobj = " + jobj);
                System.out.println(areaMdl+"코드 : "+code);
                break;
            }
        }

        System.out.println("code = " + code);
        //동 검색
        url = new URL("https://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+code+".json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = br.readLine().toString();
        br.close();
        //System.out.println(result);

        parser = new JSONParser();
        jArr = (JSONArray) parser.parse(result);

        for(int i = 0 ; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
//            System.out.println("jobj = " + jobj);

//            thirdAddressResult.put("thirdAddressName",jobj.get("value").toString());
            jsonObject.put("thirdAddressName",jobj.get("value"));
            jsonArray.add(jsonObject.clone());//TODO 해당부분 jsonArray.add(jsonObject)와 해당 코드와의 차이 찾아보기
//            System.out.println("동네목록 = " + jobj.get("value"));

        }
//        System.out.println("jsonArray = " + jsonArray);
//        System.out.println("thirdAddressResult = " + thirdAddressResult);
        return jsonArray;
    }

    @Transactional
    public void postThirdAddressName(Long addressIdx,PostThirdAddressNameReq postThirdAddressNameReq) throws BaseException {

        Address address = addressRepository.findById(addressIdx).orElse(null);
        if(postThirdAddressNameReq.getThirdAddressName()==null){
            throw  new BaseException(BaseResponseStatus.EMPTY_THIRD_ADDRESS);
        }
        address.setThirdAddressName(postThirdAddressNameReq.getThirdAddressName());
        addressRepository.save(address);



    }

    /**
     *
     * @param userIdx
     * 유저 Idx로 주소값 받기
     */
    public ArrayList getAllAddressesByUserIdx(Long userIdx) throws IOException, ParseException,BaseException {
        //관련 address 를 받음
        ArrayList arrayList=new ArrayList();
        ArrayList<Map> weatherList=new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        List<Address> addresses = addressRepository.findByUserAddress(userIdx);
        for(int i=0;i<addresses.size();i++){


            Map<String,String> homeWeather=null;
            Long addressIdx = addresses.get(i).getId();
            String firstAddressName=addresses.get(i).getFirstAddressName();
            String secondAddressName=addresses.get(i).getSecondAddressName();

            // 시,도 구 정보 받아 nx ny로 좌표 변경

            Map<String, String> nxNy = weatherService.convertNxNy(firstAddressName, secondAddressName);

            //x,y 값 얻기
            String nx=nxNy.get("x");
            String ny=nxNy.get("y");
             arrayList.addAll(postWeatherList(nx, ny, secondAddressName));

        }

        return arrayList;
    }

    /**
     * 오늘의 최고 최저 기온 정보 리스트로 넘김
     * 홈 화면 전용
     */
    public ArrayList postWeatherList(String nx,String ny,String secondAddressName) throws IOException, ParseException {
        ArrayList<Map> weatherList=new ArrayList<>();
        Map<String,String> homeWeather=new HashMap<>();
        Map<String,String> address=new HashMap<>();
        int index=0;

        //시가 존재할 경우 구 정보만 반환
        if(secondAddressName.matches(".*시.*")){
            index=secondAddressName.indexOf("시");
            secondAddressName=secondAddressName.substring(index+1);
            System.out.println("index = " + index);
        }


            address.put("secondAddressName",secondAddressName);
            homeWeather.putAll(weatherService.getTodayWeatherHighAndLow(nx, ny));
            System.out.println("현재 날씨 하늘 정보 조회 함수 ");
            homeWeather.putAll(weatherService.getTodayWeatherNow(nx,ny));
            homeWeather.putAll(dustService.getDust(secondAddressName));
            homeWeather.putAll(address);


//        System.out.println("homeWeather = " + homeWeather.toString());
         weatherList.add(homeWeather);
        return weatherList;
    }

    /**
     *  구 정보만 반환하기
     *
     */

    public String convertSecondAddressName(String secondAddressName){

        int index=0;
        //시가 존재할 경우 구 정보만 반환
        if(secondAddressName.matches(".*시.*")){
            index=secondAddressName.indexOf("시");
            secondAddressName=secondAddressName.substring(index+1);
            System.out.println("index = " + index);
        }

        return secondAddressName;
    }

}
