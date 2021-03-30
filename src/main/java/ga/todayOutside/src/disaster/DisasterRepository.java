package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.disaster.model.DisasterInfoEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DisasterRepository extends CrudRepository<DisasterInfoEntity, Integer> {
    Optional<DisasterInfoEntity> findByMsgIdx(Long msgIdx);

    @Query("select new ga.todayOutside.src.disaster.model.DisasterInfo(d.state, d.city, d.msg, d.createDate ,d.msgIdx, d.kind)" +
            " from DisasterInfoEntity d where d.createDate between :start and :end order by d.createDate desc")
    ArrayList<DisasterInfo> findAllByCreateDateBetween(@Param("start") String start, @Param("end") String end);

}
