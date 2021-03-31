package ga.todayOutside.src.address;


import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.GetAddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query("select a from Address a where a.userInfo.id= :userIdx order by a.addressOrder asc ")
    List<Address> findByUserAddress(@Param("userIdx") Long userIdx);


    @Query("select new ga.todayOutside.src.address.model.GetAddressRes(a.id,a.thirdAddressName,a.secondAddressName) from Address a where a.userInfo.id =:userIdx order by a.addressOrder asc ")
    List<GetAddressRes> findAllByUserIdx(@Param("userIdx")  Long userIdx);

    @Query("select a from  Address a where a.userInfo.id= :userIdx and a.id =:addressIdx")
    List<Address> findByUserIdxAndAddressIdx(@Param("userIdx") Long userIdx,@Param("addressIdx") Long addressIdx);




//    @Query("select Address from UserInfo u join fetch u.addressList  where u.id=:userIdx ")
//    List<Address> findByUserIdx(@Param("userIdx") Long userIdx);

    @Query("select a from Address a join fetch a.userInfo where a.userInfo.id= :userIdx")
    List<Address> findByUserIdx(@Param("userIdx") Long userIdx);


    /**
     * paul 3 / 27  작성
     * @param userIdx
     * @return
     */
    @Query("select a.id from Address a where a.userInfo.id= :userIdx")
    List<Long> findByUserIdxForAddressId(@Param("userIdx") Long userIdx);

    @Query("select a from Address a where a.userInfo.id= :userIdx and a.addressOrder = 1")
    Address findByUserIdxForFirstOrder(@Param("userIdx") Long userIdx);

}
