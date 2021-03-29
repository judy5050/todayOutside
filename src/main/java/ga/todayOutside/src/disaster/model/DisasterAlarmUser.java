package ga.todayOutside.src.disaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DisasterAlarmUser {
    private List<String> alarmKinds;
    private String targetToken;
    private String secondAddress;
}
