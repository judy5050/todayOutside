package ga.todayOutside.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String noticeAlarmStatus;
    private final Long heartNum;
    private final Long talkNum;
    private final String profile;
}