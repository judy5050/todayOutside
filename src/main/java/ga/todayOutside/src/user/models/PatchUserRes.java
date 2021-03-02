package ga.todayOutside.src.user.models;

import ga.todayOutside.src.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private final String email;
    private final String nickname;
    private String picture;
    private Role role;
}
