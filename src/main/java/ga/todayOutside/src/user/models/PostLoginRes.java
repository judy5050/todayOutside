package ga.todayOutside.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginRes {
    private final Long userId;
    private final String email;
    private final Long snsId;
}
