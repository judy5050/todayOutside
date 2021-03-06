package ga.todayOutside.src.user.models;

import ga.todayOutside.src.user.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PatchUserReq {
    private Long id;
    private String email;
    private String nickname;
    private String picture;
    private Role role;

}
