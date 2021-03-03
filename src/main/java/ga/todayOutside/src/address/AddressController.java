package ga.todayOutside.src.address;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.PostAddressReq;
import ga.todayOutside.src.address.model.PostAddressRes;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/user/address")
    public BaseResponse<PostAddressRes> postUserTown(@RequestBody PostAddressReq postAddressReq){
        Long userIdx;
        try {
            userIdx= jwtService.getUserId();
            Address address;
            System.out.println("postAddressReq = " + postAddressReq.getFirstAddressName());
            address= addressService.createAddress(userIdx,postAddressReq);
            PostAddressRes postAddressRes =new PostAddressRes(address.getId());
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
    @PostMapping
    public void getUserTown(){



    }


    /**
     * 회원 동네 삭제
     */
     @ResponseBody
    @DeleteMapping("user/town/{userIdx}")
    public void deleteUserTown(){




     }






}
