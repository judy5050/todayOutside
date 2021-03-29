package ga.todayOutside.src.notificationHistory;

import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationHistory, Long> {


    //유저 인덱스와 게시글 인덱스로 조회
    @Query("select n from NotificationHistory n where n.userInfo.id =:userIdx and n.messageBoard.id =:messageBoardIdx")
    List<NotificationHistory> findByUserIdxAndMessageBoardIdx(@Param("userIdx") Long userIdx, @Param("messageBoardIdx") Long messageBoardIdx);

    @Query("select count (n.messageBoard) from NotificationHistory n where n.messageBoard.id =:messageBoardIdx ")
    int findByMessageBoardIdx(@Param("messageBoardIdx") Long messageBoardIdx);
}
