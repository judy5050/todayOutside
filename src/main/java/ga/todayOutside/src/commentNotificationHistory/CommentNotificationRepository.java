package ga.todayOutside.src.commentNotificationHistory;

import ga.todayOutside.src.commentNotificationHistory.model.CommentNotificationHistory;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentNotificationRepository extends JpaRepository<CommentNotificationHistory,Long> {

    @Query("select c from CommentNotificationHistory c where c.userInfo.id =:userIdx and c.comment.id =:commentIdx")
    List<CommentNotificationHistory> findByUserIdxAndCommentIdx(@Param("userIdx") Long userIdx,@Param("commentIdx") Long commentIdx);
}
