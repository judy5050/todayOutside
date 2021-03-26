package ga.todayOutside.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    private final Long userId;
    private final String email;
    private final Long snsId;
    private final String jwt;
    private final List<Long> addressIds;
}
