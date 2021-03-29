package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterInfo;
import ga.todayOutside.src.disaster.model.DisasterInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DisasterRepository extends CrudRepository<DisasterInfoEntity, Integer> {
    Optional<DisasterInfoEntity> findByMsgIdx(Long msgIdx);
    ArrayList<DisasterInfo> findAllByCreateDateBetween(String start, String end);
}
