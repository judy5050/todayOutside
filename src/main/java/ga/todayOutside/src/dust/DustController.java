package ga.todayOutside.src.dust;

import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponse;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.AddressService;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import ga.todayOutside.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DustController {

    private  final JwtService jwtService;
    private  final DustService dustService;
    private final UserInfoService userInfoService;
    private final AddressService addressService;

    @ResponseBody
    @GetMapping("/address/{addressIdx}/dust")
    public BaseResponse<Map> getDust(@PathVariable Long addressIdx) throws IOException, ParseException {

        Long userIdx;
        UserInfo userInfo;
        Address address;

        try{
            userIdx = jwtService.getUserId();
            userInfo = userInfoService.findByUserIdx(userIdx);
            address = addressService.findByAddress(addressIdx, userIdx);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

        Map dust = dustService.getDust(address.getSecondAddressName());

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_DUST,dust);

    }

}
