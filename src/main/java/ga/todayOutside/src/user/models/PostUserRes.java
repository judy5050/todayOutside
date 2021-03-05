package ga.todayOutside.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserRes {
    private final Long userId;
    private final String jwt;
}
