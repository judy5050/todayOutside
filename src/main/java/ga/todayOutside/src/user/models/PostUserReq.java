package ga.todayOutside.src.user.models;

import ga.todayOutside.src.user.Role;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private Integer id;
    private String email;
    private String nickname;
    private String picture;
    private Role role;
    private String status;
}
