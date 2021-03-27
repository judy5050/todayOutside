package ga.todayOutside.src.disaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisasterFilterRes {
    private String state;
    private String city;
    private String msg;
    private String createDate;
    private Long msgIdx;
    private String kinds;
}
