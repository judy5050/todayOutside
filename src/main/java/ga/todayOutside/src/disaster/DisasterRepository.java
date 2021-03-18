package ga.todayOutside.src.disaster;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisasterRepository extends CrudRepository<DisasterInfoEntity, Integer> {
    Optional<DisasterInfoEntity> findByMsgIdx(Long msgIdx);
    ArrayList<DisasterInfoEntity> findAllByCreateDateBetween(String start, String end);
}
