package ga.todayOutside.src.user.models;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.address.model.Address;
import ga.todayOutside.src.comment.model.Comment;
//import ga.todayOutside.src.commentNotificationHistory.model.CommentNotificationHistory;
import ga.todayOutside.src.commentNotificationHistory.model.CommentNotificationHistory;
import ga.todayOutside.src.heartHistory.model.HeartHistory;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.

public class UserInfo extends BaseEntity {
    /**
     * 유저 ID
     * judy 5050작성 코드
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 닉네임
     */
    @Column(name = "userNickname", nullable = false, length = 30)
    private String nickname;

    /**
     * 이메일
     */
    @Column(name = "userEmail", length = 30)
    private String email;

    /**
     * 게시글알림선택
     */
    @Column(name = "noticeAlarmStatus", length = 1)
    private String noticeAlarmStatus;

    /**
     * 재난알림선
     */
    @Column(name = "disasterAlarmStatus", length = 30)
    private String disasterAlarmStatus;

//    /**
//     * 회원닉네임
//     */
//    @Column(name = "userMainLocation", length = 30)
//    private String userMainLocation;

//    /**
//     * 회원메인위치
//     */
//    @Column(name = "userSubLocation", length = 30)
//    private String userSubLocation;

    /**
     * 유저 사진
     */
    @Column(name = "userProfile")
    private String picture;


    /**
     * 하트 개수
     */
    @Column(name = "heartNum")
    private Long heartNum;

    /**
     * 소셜 아이디 아이디
     */
    @Column(name = "snsId", nullable = false)
    private Long snsId;

    /**
     *주소 조회
     */
    @OneToMany(mappedBy = "userInfo")
    List<Address> addressList=new ArrayList<>();

    /**
     * 댓글
     */
    @OneToMany(mappedBy = "userInfo")
    List<Comment> comments=new ArrayList<>();

    /**
     * 하트
     */
    @OneToMany(mappedBy = "userInfo")
    List<HeartHistory> heartHistories=new ArrayList<>();

    /**
     * 게시글 신고
     */
    @OneToMany(mappedBy = "userInfo")
    List<NotificationHistory> notificationHistories=new ArrayList<>();

    /**
     * 댓글 신고
     */
    @OneToMany(mappedBy = "userInfo")
    List<CommentNotificationHistory> commentNotificationHistories=new ArrayList<>();


    /**
     *  나눔 이야기 수
     */
    @Column(name = "talkNum")
    private Long talkNum;

    /**
     * 삭제 여부
     */
    @Column(name = "isDeleted")
    private String isDeleted;

//    @Builder
//    public UserInfo(Long id, String nickname,
//                    String email, String noticeAlarmStatus,
//                    String disasterAlarmStatus, String userMainLocation,
//                    String userSubLocation, String picture,
//                    String isDeleted, Long heartNum, Long snsId) {
//        this.id = id;
//        this.nickname = nickname;
//        this.email = email;
//        this.noticeAlarmStatus = noticeAlarmStatus;
//        this.disasterAlarmStatus = disasterAlarmStatus;
////        this.userMainLocation = userMainLocation;
////        this.userSubLocation = userSubLocation;
//        this.picture = picture;
//        this.isDeleted = isDeleted;
//        this.heartNum = heartNum;
//        this.snsId = snsId;
//    }

    //0이면 참여 x 1이면 참여
    @Column(name="messageBoardStatus")
    private Integer messageBoardStatus;


    @Builder
    public UserInfo(Long id, String nickname,
                    String email, String noticeAlarmStatus,
                    String disasterAlarmStatus,String picture,
                    Long heartNum, Long snsId, Long talkNum, String isDeleted) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.noticeAlarmStatus = noticeAlarmStatus;
        this.disasterAlarmStatus = disasterAlarmStatus;
        this.picture = picture;
        this.heartNum = heartNum;
        this.snsId = snsId;
        this.talkNum = talkNum;
        this.isDeleted = isDeleted;
    }

}
