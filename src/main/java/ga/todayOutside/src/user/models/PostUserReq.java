package ga.todayOutside.src.user.models;

import ga.todayOutside.src.address.model.PostAddressReq;
import ga.todayOutside.src.user.Role;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private Integer id;
    private String email;
    private String nickname;
    private String picture;
    private List<PostAddressReq> addressInfos;
    private Long snsId;

    @Builder
    public PostUserReq(String email, String nickname, String picture,
                       List<PostAddressReq> addressInfos, Long snsId) {

        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.addressInfos = addressInfos;
        this.snsId = snsId;
    }
}
