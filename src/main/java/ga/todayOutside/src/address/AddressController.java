package ga.todayOutside.src.address;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.*;
import ga.todayOutside.src.user.UserInfoRepository;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AddressController {


    private final AddressService addressService;
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;

    /**
     *
     * 임시로 jwt사용 용도
     */

    @ResponseBody
    @GetMapping("/jwt")
    public String getJwt (){
        String jwt=jwtService.createJwt(1L);
        return jwt;
    }




    /**
     * 회원 동네 등록
     */
    @ResponseBody
    @PostMapping("/addresses")
    public BaseResponse<PostAddressRes> postAddress(@RequestBody PostAddressReq postAddressReq){
        Long userIdx;
        try {
            userIdx= jwtService.getUserId();
            Address address;
            address= addressService.createAddress(userIdx,postAddressReq);
            PostAddressRes postAddressRes =new PostAddressRes(address.getId(),address.getAddressOrder());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_ADDRESS, postAddressRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     * 회원 동네 조회
     */
    @ResponseBody
    @GetMapping("/addresses")
    public BaseResponse<List<GetAddressRes>> getAddressList(){

        Long userIdx;
        try {
            userIdx =jwtService.getUserId();
            System.out.println("userIdx = " + userIdx);

        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

        try {
            List<GetAddressRes> getAddressResList =addressService.addressByUserIdx(userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_ADDRESS,getAddressResList);//
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }




    }


    /**
     * 회원 동네 삭제
     */
     @ResponseBody
     @DeleteMapping("/addresses/{addressIdx}")
    public BaseResponse deleteAddress(@PathVariable Long addressIdx){


         Long userIdx;


         try {
             userIdx = jwtService.getUserId();
         } catch (BaseException exception) {
             return new BaseResponse<>(exception.getStatus());
         }

         //user idx 와 입력받은 addressIdx 일치 여부 확인 및 address 반환
         try {
               addressService.findByAddress(addressIdx, userIdx);
         }
         catch (BaseException exception){
             return new BaseResponse<>(exception.getStatus());
         }

         try{
             addressService.deleteAddress(addressIdx);
             addressService.bulkAddressOrder(userIdx);
         }
         catch (BaseException exception){
            return new BaseResponse(exception.getStatus());
         }
         //삭제 후 addressOrder 의 모든 행들 -1 해주기


        return new BaseResponse(BaseResponseStatus.SUCCESS_DELETE_ADDRESS);
     }

    /**
     * 회원 구 이름 수정
     */

    @ResponseBody
    @PatchMapping("/addresses/{addressIdx}")
    public BaseResponse patchAddress(@PathVariable Long addressIdx, @RequestBody PatchAddressNameReq patchAddressNameReq){

        Long userIdx;
        Address address;

        try {
            userIdx = jwtService.getUserId();
            //user idx 와 입력받은 addressIdx 일치 여부 확인 및 address 반환
            address = addressService.findByAddress(addressIdx, userIdx);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        addressService.patchAddressName(address,patchAddressNameReq);









        return new BaseResponse(BaseResponseStatus.SUCCESS_PATCH_ADDRESS_NAME);
    }

    /**
     * 회원 동네 순서 수정
     */
    @ResponseBody
    @PatchMapping("/address/{addressIdx}/addressOrder")
    public BaseResponse<Void>patchAddressOrder(@PathVariable Long addressIdx,@RequestBody PatchAddressOrder patchAddressOrder){

        Long userIdx;
        Address address;
        Integer maxAddressNum=2;

        try {
            //jwt 토큰값으로 유저 확인
            userIdx = jwtService.getUserId();
            //addressIdx 와 유저의 addressIdx 가 같은지 확인
            address = addressService.findByAddress(addressIdx, userIdx);
//            if(patchAddressOrder==null){
//                //patchAddressOrder 이 공백일 경우
//                return new BaseResponse<>(BaseResponseStatus.EMPTY_ADDRESS_ORDER);
//            }
            //순서 변경 요청값이 0일 경우 또는 최대 주소 등록개수를 초과할 경우 (현재는 2개가 최대)//TODO 주소 최대 등록개수 증가할 경우 값 변경하기
             if(patchAddressOrder.getAddressOrder()==0||patchAddressOrder.getAddressOrder()>maxAddressNum){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ADDRESS_ORDER);
            }
            else{
                addressService.patchAddressOrder(userIdx,addressIdx,patchAddressOrder,maxAddressNum);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_ADDRESS_ORDER);
    }


    /**
     * 회원 구 정보에 맞는 동네 목록 조회
     */
    @ResponseBody
    @GetMapping("/address/{addressIdx}/thirdAddressNameList")
    public BaseResponse<JSONArray>getThirdAddressNameList(@PathVariable Long addressIdx) throws IOException, ParseException {

        Long userIdx;
        Address address;
        JSONArray thirdAddressesName;

        try {
            //jwt 토큰값으로 유저 확인
            userIdx = jwtService.getUserId();
            //addressIdx 와 유저의 addressIdx 가 같은지 확인
            address = addressService.findByAddress(addressIdx, userIdx);

            //동네 리스트 조회
             thirdAddressesName = addressService.getThirdAddressesName(address.getFirstAddressName(), address.getSecondAddressName());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_THIRD_ADDRESS_NAME,thirdAddressesName);
    }

    /**
     * 구 정보에 맞는 동네 등록 API
     */

    @ResponseBody
    @PostMapping("/address/{addressIdx}/thirdAddressName")
    public BaseResponse<Void>postThirdAddressName(@PathVariable Long addressIdx,@RequestBody PostThirdAddressNameReq postThirdAddressNameReq){

        Long userIdx;
        Address address;
        UserInfo userInfo;

        try {
            //jwt 토큰값으로 유저 확인
            userIdx = jwtService.getUserId();
            //addressIdx 와 유저의 addressIdx 가 같은지 확인
            address = addressService.findByAddress(addressIdx, userIdx);
            addressService.postThirdAddressName(addressIdx,postThirdAddressNameReq);
            userInfo = userInfoRepository.findById(userIdx).orElse(null);

            //게시글 참여로 유저 상태 수정
            userInfo.setMessageBoardStatus(1);
            userInfoRepository.save(userInfo);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }


        return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_THIRD_ADDRESS_NAME);
    }



}
