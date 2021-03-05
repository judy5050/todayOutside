package ga.todayOutside.src.user.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final Long userId;
    private final String email;
}
