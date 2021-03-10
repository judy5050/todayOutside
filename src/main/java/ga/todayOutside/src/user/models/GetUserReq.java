package ga.todayOutside.src.user.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUserReq {
    private final Long userId;
    private final String email;
}
