package ga.todayOutside.src.user.auth.dto;

import ga.todayOutside.src.user.models.UserInfo;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String picture;

    public SessionUser(UserInfo userInfo) {
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.picture = userInfo.getPicture();
    }
}
