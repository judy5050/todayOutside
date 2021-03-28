package ga.todayOutside.src.disaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class DisasterInfo {

    private String state;
    private String city;
    private String msg;
    private String createDate;
    private Long msgIdx;
    private String kind;

}
