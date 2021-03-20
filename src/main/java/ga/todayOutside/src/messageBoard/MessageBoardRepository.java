package ga.todayOutside.src.messageBoard;


import ga.todayOutside.src.messageBoard.models.BoardType;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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



    //구에 필터링 된 게시글만 조회
    @Query("select m from MessageBoard  m where m.addressMsg like  %:filter%  and m.boardType =:boardType order by m.heartNum desc ")
    Page<MessageBoard> findByAddressMsgLike(@Param("filter") String filter, Pageable pageable,@Param("boardType") BoardType boardType);



}