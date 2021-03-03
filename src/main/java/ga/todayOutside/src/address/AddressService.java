//package ga.todayOutside.src.address;
//
//
//
//import ga.todayOutside.config.BaseException;
//import ga.todayOutside.src.address.model.Address;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class AddressService {
//
//
//    final AddressRepository addressRepository;
//
//    /**
//     *회원 주소 등록
//     */
//    @Transactional
//    public void createAddress(String firstAddressName, String secondAddressName, String thirdAddressName) throws BaseException{
//
//        //회원 정보 가져와서 메인 주소지가 있는지 확인
//        //있을경우 서브 주소로 추가
//        //없을경우 메인 주소로 추가
//        Address address=new Address(firstAddressName,secondAddressName,thirdAddressName);
//        addressRepository.save(address);
//
//
//
//
//
//    }
//
//
//}
