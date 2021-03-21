package ga.todayOutside.src.heartHistory;


import ga.todayOutside.src.heartHistory.model.HeartHistory;
import ga.todayOutside.src.heartHistory.model.HeartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HeartHistoryRepository extends JpaRepository<HeartHistory,Long> {

    @Query("select h from HeartHistory h join fetch h.userInfo u where u.id =:userIdx and h.messageBoard.id =:messageIdx")
    Optional<HeartHistory> findByUserIdxAndMessageIdx(@Param("userIdx")Long userIdx, @Param("messageIdx")Long messageIdx);
}
