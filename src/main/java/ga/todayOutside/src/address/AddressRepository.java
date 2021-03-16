package ga.todayOutside.src.address;


import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.address.model.GetAddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query("select a from Address a where a.userInfo.id= :userIdx")
    List<Address> findByUserAddress(@Param("userIdx") Long userIdx);

    @Query("select new ga.todayOutside.src.address.model.GetAddressRes(a.id,a.firstAddressName,a.secondAddressName,a.thirdAddressName) from Address a")
    List<GetAddressRes> findAllByUserIdx(Long userIdx);

    @Query("select a from  Address a where a.userInfo.id= :userIdx and a.id =:addressIdx")
    List<Address> findByUserIdxAndAddressIdx(@Param("userIdx") Long userIdx,@Param("addressIdx") Long addressIdx);
}
