package ga.todayOutside.src.user.models;

import ga.todayOutside.src.user.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PatchUserReq {

    private String nickname;
    private String email;
    private String picture;

}
