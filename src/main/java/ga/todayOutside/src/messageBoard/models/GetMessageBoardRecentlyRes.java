package ga.todayOutside.src.messageBoard.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@NoArgsConstructor
@Setter
public class GetMessageBoardRecentlyRes {

    private Long messageBoardIdx;
    private String picture;
    private String userNickName;
    private String thirdAddressName;
    private String msg;
    private String heartNum;
    private String commentNum; //댓글 수
    private String date;
    private String isExistent;


    public GetMessageBoardRecentlyRes(MessageBoard messageBoard) {
        int size=0;
        if(messageBoard==null){
            this.isExistent="N";
        }
        else{
            this.isExistent="Y";
        }
        this.messageBoardIdx=messageBoard.getId();
        this.userNickName=messageBoard.getUserInfo().getNickname();
        this.msg= messageBoard.getMessage();
        this.heartNum=messageBoard.getHeartNum().toString();

        String[]array=messageBoard.getAddressMsg().split(" ");
        if(array.length>1){
            this.thirdAddressName=array[(array.length)-1];
        }
        else{
            this.thirdAddressName=getThirdAddressName();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date= simpleDateFormat.format(messageBoard.getCreatedAt());

        this.commentNum = Integer.toString(messageBoard.getComments().size());
        this.picture= messageBoard.getUserInfo().getPicture();



    }

    public GetMessageBoardRecentlyRes(String isExistent) {
        this.isExistent=isExistent;
    }
}
