package ga.todayOutside.src.disaster.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DisasterInfo {
    private String state;
    private String city;
    private String msg;
    private String createDate;
    private String msgIdx;

    public DisasterInfo(String state, String city, String msg, String createDate, String msgIdx) {
        this.state = state;
        this.city = city;
        this.msg = msg;
        this.createDate = createDate;
        this.msgIdx = msgIdx;
    }
}
