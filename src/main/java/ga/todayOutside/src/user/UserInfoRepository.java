package ga.todayOutside.src.user;

import ga.todayOutside.src.address.model.GetAddressRes;
import ga.todayOutside.src.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    List<UserInfo> findByStatus(String status);
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findByEmailAndStatus(String email, String status);
    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);

}