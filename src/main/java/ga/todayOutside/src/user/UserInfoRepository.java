package ga.todayOutside.src.user;

import ga.todayOutside.src.user.models.PatchUserReq;
import ga.todayOutside.src.user.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
//    List<UserInfo> findByStatus(String status);
    Optional<UserInfo> findByEmail(String email);
//    List<UserInfo> findBySnsIdAndStatus(Long snsId, String status);
//    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
    Optional<UserInfo> findBySnsId(Long snsId);
    Optional<UserInfo> findById(Long id);
    Integer deleteById(Long userId);
    Optional<UserInfo> save(PatchUserReq patchUserReq);

    //주디 작성
    //유저 하트수 증가
    @Modifying
    @Query("update UserInfo u set u.heartNum=u.heartNum+1 where u.id =:userIdx")
    void updateUserHeartPlus(@Param("userIdx") Long userIdx);

    //주디 작성
    //유저 하트수 감소
    @Modifying
    @Query("update UserInfo u set u.heartNum=u.heartNum-1 where u.id =:userIdx")
    void updateUserHeartSub(@Param("userIdx") Long userIdx);

    //Integer delete(String email);

}
