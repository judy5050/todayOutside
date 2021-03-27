package ga.todayOutside.src.commentNotificationHistory.model;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CommentNotificationHistory")
public class CommentNotificationHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentNotificationIdx")
    private Long id;


    /**
     * 회원엔티티
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;


    /**
     * 댓글 엔티티
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentIdx")
    private Comment comment;


    public CommentNotificationHistory(Comment comment, UserInfo userInfo) {

        this.comment=comment;
        this.userInfo=userInfo;

    }
}
