package ga.todayOutside.src.disaster.model;

import lombok.Data;

import java.util.List;

@Data
public class DisasterAlarmReq {
    private List<String> name;
    private Long userIdx;
}
