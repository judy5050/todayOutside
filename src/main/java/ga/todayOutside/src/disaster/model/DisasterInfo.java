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
    private Long msgIdx;

    @Override
    public String toString() {
        return "DisasterInfo{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", msg='" + msg + '\'' +
                ", createDate='" + createDate + '\'' +
                ", msgIdx=" + msgIdx +
                '}';
    }
}
