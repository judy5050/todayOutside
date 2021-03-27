package ga.todayOutside.src.comment;

import ga.todayOutside.src.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c  from Comment  c where  c.messageBoard.id =:messageIdx and c.isDeleted =:isDeleted order by c.createdAt asc ")
    Page<Comment> findAllByMessageId(@Param("messageIdx")Long messageIdx, Pageable pageable,@Param("isDeleted") String N);

    @Query("select c  from Comment  c where  c.userInfo.id =:userIdx and c.isDeleted =:isDeleted order by c.createdAt desc ")
    Page<Comment> findAllByUserIdx(@Param("userIdx") Long userIdx, Pageable pageable,@Param("isDeleted")String N);

}
