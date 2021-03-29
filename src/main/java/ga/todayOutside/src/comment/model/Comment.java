package ga.todayOutside.src.comment.model;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.commentNotificationHistory.model.CommentNotificationHistory;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name="Comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



    /**
     *신고 기록
     */
    @OneToMany(mappedBy = "comment")
    private List<CommentNotificationHistory> commentNotificationHistories=new ArrayList<>();

    @Builder
    public Comment(UserInfo userInfo, String addressMsg, String commentMsg, MessageBoard messageBoard) {
        this.userInfo = userInfo;
        this.addressMsg = addressMsg;
        this.commentMsg = commentMsg;
        this.messageBoard = messageBoard;
    }
}
