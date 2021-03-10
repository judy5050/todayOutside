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

    @Override
    public String toString() {
        return "DisasterInfo{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
