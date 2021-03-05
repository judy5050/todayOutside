package ga.todayOutside.src.user.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostLoginReq {
    private String email;
    private Long snsId;
}
