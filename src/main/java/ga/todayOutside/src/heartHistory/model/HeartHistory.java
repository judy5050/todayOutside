package ga.todayOutside.src.heartHistory.model;


import ga.todayOutside.config.BaseEntity;
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
@Table(name="HeartHistory")
public class HeartHistory extends BaseEntity {

    /**
     * 하트 인덱스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="heartIdx")
    private Long id;


    /**
     * 회원엔티티
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    /**
     * 하트 눌림여부 확인
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "heartStatus")
    private HeartStatus heartStatus;


    /**
     * 게시판
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageBoardIdx")
    private MessageBoard messageBoard;


    public HeartHistory(UserInfo userInfo, MessageBoard messageBoard, HeartStatus heartStatus) {
        this.userInfo=userInfo;
        this.messageBoard=messageBoard;
        this.heartStatus=heartStatus;

    }
}
