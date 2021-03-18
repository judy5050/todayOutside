package ga.todayOutside.src.address;



import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.address.model.*;
import ga.todayOutside.src.user.UserInfoRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {


    final AddressRepository addressRepository;
    final UserInfoService userInfoService;
    final UserInfoRepository userInfoRepository;
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
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        try {
            address=addressRepository.findAllByUserIdx(userIdx);
            if(address.isEmpty()){
                throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);
            }
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADDRESS);//TODO 오류 값 바꾸기
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
        }

        addressRepository.save(address1);

    }

    /**
     * 주소 삭제 후 남은 address 의 addressOrder 수정하기
     */

    @Transactional
    public  void bulkAddressOrder(Long userIdx)throws BaseException{
        boolean check=false;
        Address address;

         List<Address> addressList = addressRepository.findByUserIdx(userIdx);
            if(addressList.size()>=1){
                for(int i=0;i<addressList.size();i++){
                   address = addressList.get(i);
                   addressRepository.bulkAddressOrder(address.getId());


                }
        }




    }


}
