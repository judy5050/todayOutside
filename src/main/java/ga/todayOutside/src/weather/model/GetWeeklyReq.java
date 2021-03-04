package ga.todayOutside.src.weather.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GetWeeklyReq {

    private String dataType;
    String baseTime = "0800";    //API 제공 시간
}
