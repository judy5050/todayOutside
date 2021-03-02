package ga.todayOutside.src.user.auth.dto;

import ga.todayOutside.src.user.Role;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;

    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("apple".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofKakao("id", attributes);
    }

    public static OAuthAttributes ofKakao(String userNameAttributesName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributesName)
                .build();
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .email(email)
                .nickname(nickname)
                .picture(picture)
                .role(Role.GUEST)
                .build();

    }

}
