package ga.todayOutside.src.address;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.*;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {


    private final AddressService addressService;
    private final JwtService jwtService;


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
    @PostMapping("/address")
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

        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

        try {
            List<GetAddressRes> getAddressResList =addressService.addressByUserIdx(userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_ADDRESS,getAddressResList);//TODO 에러 메시지 수정하기
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
     * 회원 동네 이름 수정
     */

    @ResponseBody
    @PatchMapping("/addresses/{addressIdx}")
    public BaseResponse patchAddress(@PathVariable Long addressIdx, @RequestBody PatchAddressNameReq patchAddressNameReq){

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

        addressService.patchAddressName(address,patchAddressNameReq);





        return new BaseResponse(BaseResponseStatus.SUCCESS_PATCH_ADDRESS_NAME);
    }





}
