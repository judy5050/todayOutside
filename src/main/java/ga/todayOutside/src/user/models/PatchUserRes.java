package ga.todayOutside.src.user.models;

import ga.todayOutside.src.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private final Long userId;
    private final String nickname;
    private final String picture;
    private final String email;

}
