package ga.todayOutside.src.messageBoard.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@NoArgsConstructor
@Getter
public class GetMessageBoardListHeartRes {

    private Long messageBoardIdx;
    private String userNickName;
    private String thirdAddressName;
    private String msg;
    private Long heartNum;
    private int commentNum; //댓글 수
    private String date;


    public GetMessageBoardListHeartRes(MessageBoard messageBoard) {
        this.messageBoardIdx=messageBoard.getId();
        this.userNickName = messageBoard.getUserInfo().getNickname();
        this.msg = messageBoard.getMessage();
        this.heartNum = messageBoard.getHeartNum();
        String[] array = messageBoard.getAddressMsg().split(" ");
        if (array.length > 1) {
            this.thirdAddressName = array[(array.length) - 1];
        } else {
            this.thirdAddressName = getThirdAddressName();
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        this.date = simpleDateFormat.format(messageBoard.getCreatedAt());
        this.commentNum = messageBoard.getComments().size();

    }
}