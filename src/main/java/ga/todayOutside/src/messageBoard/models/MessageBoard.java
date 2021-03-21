package ga.todayOutside.src.messageBoard.models;


import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="MessageBoard")
public class MessageBoard extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageBoardIdx")
    private Long id;

    /**
     * 유저와 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    /**
     * 주소
     */

    @Column(name = "addressMsg")
    private String addressMsg;

    /**
     *댓글
     */
    @OneToMany(mappedBy = "messageBoard")
    private List<Comment> comments=new ArrayList<>();


    /**
     * 게시글
     */
    @Column(name = "message")
    private String message;

    /**
     * 게시글 하트 수
     */
    @Column(name="heartNum")
    private Long heartNum;

    /**
     * 게시글 종류
     */
    @Column(name = "boardType")
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    public MessageBoard(UserInfo userInfo,String addressMsg,String message,Long heartNum) {
        this.userInfo=userInfo;
        this.addressMsg=addressMsg;
        this.message=message;
        this.heartNum=heartNum;

    }


    public MessageBoard(UserInfo userInfo, String addressMsg, String message, long heartNum, BoardType boardType) {

        this.userInfo=userInfo;
        this.addressMsg=addressMsg;
        this.message=message;
        this.heartNum=heartNum;
        this.boardType=boardType;
    }
}
