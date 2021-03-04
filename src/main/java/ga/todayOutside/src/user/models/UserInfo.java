package ga.todayOutside.src.user.models;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.user.Role;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
@Getter
public class UserInfo extends BaseEntity {
    /**
     * 유저 ID
     * judy 5050작성 코드
     */

//    @Id // PK를 의미하는 어노테이션
//    @Column(name = "userIdx", nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    /**
     * 유저 ID
     */

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 이메일
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;


    /**
     * 닉네임
     */
    @Column(name = "nickname", nullable = true, length = 30)
    private String nickname;

    /**
     * 상태
     */
    @Column(name = "status", nullable = true, length = 10)
    private String status = "ACTIVE";

    @Column(name = "picture")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public UserInfo(String email, String nickname, String picture, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.role = role;
    }

    public UserInfo update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() { return this.role.getKey(); }

}
