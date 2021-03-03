//package ga.todayOutside.src.address;
//
//
//import ga.todayOutside.config.BaseException;
//import ga.todayOutside.config.BaseResponse;
//import ga.todayOutside.config.BaseResponseStatus;
//import ga.todayOutside.src.address.model.Address;
//import ga.todayOutside.src.address.model.GetAddressRes;
//import ga.todayOutside.src.address.model.PostAddressReq;
//import ga.todayOutside.src.address.model.PostAddressRes;
//import ga.todayOutside.src.user.models.UserInfo;
//import ga.todayOutside.utils.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class AddressController {
//
//
//    private final AddressService addressService;
//    private final JwtService jwtService;
//
//
//    /**
//     *
//     * 임시로 jwt사용 용도
//     */
//
//    @ResponseBody
//    @GetMapping("/jwt")
//    public String getJwt (){
//        String jwt=jwtService.createJwt(1L);
//        return jwt;
//    }
//
//
//
//
//    /**
//     * 회원 동네 등록
//     */
//    @ResponseBody
//    @PostMapping("/user/address")
//    public BaseResponse<PostAddressRes> postAddress(@RequestBody PostAddressReq postAddressReq){
//        Long userIdx;
//        try {
//            userIdx= jwtService.getUserId();
//            Address address;
//            System.out.println("postAddressReq = " + postAddressReq.getFirstAddressName());
//            address= addressService.createAddress(userIdx,postAddressReq);
//            PostAddressRes postAddressRes =new PostAddressRes(address.getId());
//            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_ADDRESS, postAddressRes);
//        }
//        catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//    }
//
//
//    /**
//     * 회원 동네 조회
//     */
//    @ResponseBody
//    @GetMapping("/user/address")
//    public BaseResponse<List<GetAddressRes>> getAddressList(){
//
//        Long userIdx;
//        try {
//            userIdx =jwtService.getUserId();
//
//        }catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//        try {
//            List<GetAddressRes> getAddressResList =addressService.addressByUserIdx(userIdx);
//            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_ADDRESS,getAddressResList);//TODO 에러 메시지 수정하기
//        }catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//
//
//
//    }
//
//
//    /**
//     * 회원 동네 삭제
//     */
//     @ResponseBody
//     @DeleteMapping("user/address/{addressIdx}")
//    public BaseResponse deleteAddress(@PathVariable Long addressIdx){
//
//         System.out.println("addressIdx = " + addressIdx);
//         try{
//             addressService.deleteAddress(addressIdx);
//         }
//         catch (BaseException exception){
//            return new BaseResponse(exception.getStatus());
//         }
//
//
//        return new BaseResponse(BaseResponseStatus.SUCCESS_DELETE_ADDRESS);
//     }
//
//
//
//
//
//
//}
