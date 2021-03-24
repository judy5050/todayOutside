package ga.todayOutside.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostKakaoLoginRes {

    private final  Long userIdx;
    private final String jwt;
    private final String email;
    private final Long snsId;
}
