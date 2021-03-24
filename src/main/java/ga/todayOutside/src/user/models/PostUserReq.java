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
    private String firstAddressName;
    private String secondAddressName;
    private Long snsId;

    @Builder
    public PostUserReq(String email,
                       String nickname, String picture,
                       String firstAddressName, String secondAddressName,
                       Long snsId) {

        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.firstAddressName = firstAddressName;
        this.secondAddressName = secondAddressName;
        this.snsId = snsId;
    }
}
