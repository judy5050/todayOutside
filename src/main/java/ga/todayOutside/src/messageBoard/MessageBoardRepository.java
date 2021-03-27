package ga.todayOutside.src.messageBoard;


import ga.todayOutside.src.messageBoard.models.BoardType;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MessageBoardRepository extends JpaRepository<MessageBoard,Long> {


    @Query("select m from MessageBoard  m   where m.userInfo.id =:userIdx  ")
     Page<MessageBoard> findByUserIdx(@Param("userIdx") Long userIdx,Pageable pageable);



    //구에 필터링 된 게시글만 조회
    @Query("select m from MessageBoard  m  where m.addressMsg like  %:filter%  and m.boardType =:boardType and m.heartNum>0 and m.isDeleted =:isDeleted and m.createdAt =: todayStr order by m.heartNum desc ")
    Page<MessageBoard> findByAddressMsgLike(@Param("filter") String filter, Pageable pageable,@Param("boardType") BoardType boardType,@Param("isDeleted")String N);

    @Query("select m from MessageBoard  m where m.addressMsg like  %:filter%  and m.boardType =:boardType and m.isDeleted =:isDeleted order by m.createdAt desc ")
    Page<MessageBoard> findByAddressRecentlyMsg(@Param("filter") String filter, Pageable pageable,@Param("boardType") BoardType boardType,@Param("isDeleted")String N);



//    @Modifying(clearAutomatically = true)
//    @Query(" update MessageBoard m set m.heartNum = m.heartNum+1  where m.id =: messageBoardIdx ")
//    int setHeartNumPlus(@Param("messageBoardIdx") Long messageBoardIdx);

//
//    @Modifying(clearAutomatically = true)
//    @Query(" update MessageBoard  m set m.heartNum = m.heartNum-1 where m.id =: messageBoardIdx ")
//    int  setHeartNumSub(@Param("messageBoardIdx") Long messageBoardIdx);

    @Modifying(clearAutomatically = true)
    @Query("update MessageBoard m set m.heartNum=m.heartNum+1 where m.id=:messageBoardIdx")
    int setHeartNumPlus(@Param("messageBoardIdx")Long messageBoardIdx);

    @Modifying(clearAutomatically = true)
    @Query("update MessageBoard m set m.heartNum=m.heartNum-1 where m.id=:messageBoardIdx")
    int setHeartNumSub(@Param("messageBoardIdx")Long messageBoardIdx);

    @Query("select m from MessageBoard m where m.id =:messageBoardIdx")
    MessageBoard findByMessage(@Param("messageBoardIdx") Long messageBoardIdx);

}
