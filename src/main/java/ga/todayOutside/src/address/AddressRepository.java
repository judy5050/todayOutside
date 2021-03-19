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

    @Query("select a from Address a where a.userInfo.id= :userIdx")
    List<Address> findByUserAddress(@Param("userIdx") Long userIdx);


    @Query("select new ga.todayOutside.src.address.model.GetAddressRes(a.id,a.secondAddressName,a.addressOrder) from Address a")
    List<GetAddressRes> findAllByUserIdx(Long userIdx);

    @Query("select a from  Address a where a.userInfo.id= :userIdx and a.id =:addressIdx")
    List<Address> findByUserIdxAndAddressIdx(@Param("userIdx") Long userIdx,@Param("addressIdx") Long addressIdx);


//    @Query("select Address from UserInfo u join fetch u.addressList  where u.id=:userIdx ")
//    List<Address> findByUserIdx(@Param("userIdx") Long userIdx);

    @Query("select a from Address a join fetch a.userInfo where a.userInfo.id= :userIdx")
    List<Address> findByUserIdx(@Param("userIdx") Long userIdx);

    @Modifying(clearAutomatically = true)
    @Query("update Address a set a.addressOrder=a.addressOrder-1 where a.id=:addressIdx")
    int subBulkAddressOrder(@Param("addressIdx")Long addressIdx);


    @Modifying(clearAutomatically = true)
    @Query("update Address a set a.addressOrder=a.addressOrder+1 where a.id=:addressIdx")
    int plusBulkAddressOrder(@Param("addressIdx")Long addressIdx);

}
