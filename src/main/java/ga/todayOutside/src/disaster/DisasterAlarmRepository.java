package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterAlarm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisasterAlarmRepository extends CrudRepository<DisasterAlarm, Integer> {
    Optional<DisasterAlarm> findByUserIdx(Long userIdx);
}
