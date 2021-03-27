package ga.todayOutside.src.disaster;

import ga.todayOutside.src.disaster.model.DisasterAlarm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterAlarmRepository extends CrudRepository<DisasterAlarm, Integer> {
    DisasterAlarm findByUserIdx(Long userIdx);
}
