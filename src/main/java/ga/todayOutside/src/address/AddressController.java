//package ga.todayOutside.src.address;
//
//
//import ga.todayOutside.config.BaseException;
//import ga.todayOutside.config.BaseResponse;
//import ga.todayOutside.config.BaseResponseStatus;
//import ga.todayOutside.src.address.model.PostUserRes;
//import ga.todayOutside.src.address.model.PostUserTownReq;
//import ga.todayOutside.src.user.models.UserInfo;
//import ga.todayOutside.utils.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
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
//    @PostMapping("user/town")
//    public BaseResponse<PostUserRes> postUserTown(PostUserTownReq postUserTownReq){
//
//      Long userIdx;
//      UserInfo userInfo;
//        try
//        {
//           userIdx= jwtService.getUserId();
//
//           addressService.createAddress(postUserTownReq.getFirstAddressName(),postUserTownReq.getSecondAddressName(),postUserTownReq.getThirdAddressName());
//
//            PostUserRes postUserRes=new PostUserRes(1);
//
//            return new BaseResponse<>(BaseResponseStatus.SUCCESS,postUserRes);
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
//    @PostMapping
//    public void getUserTown(){
//
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
//    @DeleteMapping("user/town/{userIdx}")
//    public void deleteUserTown(){
//
//
//
//
//     }
//
//
//
//
//
//
//}
