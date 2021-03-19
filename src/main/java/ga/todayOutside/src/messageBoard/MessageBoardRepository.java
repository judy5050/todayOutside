package ga.todayOutside.src.messageBoard;


import ga.todayOutside.src.messageBoard.models.MessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MessageBoardRepository extends JpaRepository<MessageBoard,Long> {

    @Query("select m from MessageBoard  m   where m.userInfo.id =:userIdx  ")
     List<MessageBoard> findByUserIdx(@Param("userIdx") Long userIdx);


}
