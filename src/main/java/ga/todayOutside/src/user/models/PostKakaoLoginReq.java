package ga.todayOutside.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostKakaoLoginReq {

    private String accessToken;
}
