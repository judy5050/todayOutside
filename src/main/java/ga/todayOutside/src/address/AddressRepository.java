package ga.todayOutside.src.address;


import ga.todayOutside.src.address.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressRepository extends CrudRepository<Address,Long> {





}
