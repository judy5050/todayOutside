package ga.todayOutside.src.comment.model;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="Comment")
public class Comments extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "commentIdx")
    private Long id;

    /**
     * 회원과 다대 일 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private UserInfo userInfo;


    /**
     * 주소
     */
    @Column(name="addressMsg")
    private String addressMsg;

    /**
     * 댓글 내용
     */
    @Column(name = "commentMsg")
    private String commentMsg;

    /**
     * 게시글과 다대 일
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageBoardIdx")
    private MessageBoard messageBoard;

}
