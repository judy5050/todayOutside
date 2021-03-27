package ga.todayOutside.src.notificationHistory.model;


import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.heartHistory.model.HeartStatus;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="NotificationHistory")
public class NotificationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="notificationIdx")
    private Long Id;

    /**
     * 회원엔티티
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;


    /**
     * 게시판
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageBoardIdx")
    private MessageBoard messageBoard;


    public NotificationHistory(MessageBoard messageBoard, UserInfo userInfo) {
        this.messageBoard=messageBoard;
        this.userInfo=userInfo;



    }
}
