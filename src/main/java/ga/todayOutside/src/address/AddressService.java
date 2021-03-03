package ga.todayOutside.src.address;



import ga.todayOutside.config.BaseException;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.AddressType;
import ga.todayOutside.src.address.model.PostAddressReq;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {


    final AddressRepository addressRepository;
    final UserInfoService userInfoService;
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

        //addressList 가 이미 저장된 것이 있는 경우 ->그 다음 저장 주소는 sub
        if(addressList.size()==1){
             address=new Address(userInfo,postAddressReq.getFirstAddressName(), postAddressReq.getSecondAddressName(), postAddressReq.getThirdAddressName(), AddressType.SUB);
             addressRepository.save(address);

        }

        //addressList 가 저장된 것이 없는 경우 ->그 다음 저장 주소는 main
        else if(addressList.size()==0){
             address=new Address(userInfo,postAddressReq.getFirstAddressName(), postAddressReq.getSecondAddressName(), postAddressReq.getThirdAddressName(),AddressType.MAIN);
            System.out.println("address = " + address.getFirstAddressName());
            addressRepository.save(address);

        }



        return address;


    }


}
