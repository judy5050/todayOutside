package ga.todayOutside.src.user.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostNewKakaoUserRes {

    private  String email;
    private  Long snsId;

    public PostNewKakaoUserRes(Long id, String email) {
        this.email=email;
        this.snsId=id;
    }
}
