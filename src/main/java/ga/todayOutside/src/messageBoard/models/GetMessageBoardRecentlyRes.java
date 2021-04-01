package ga.todayOutside.src.messageBoard.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Setter
@Getter
@NoArgsConstructor
public class GetMessageBoardRecentlyRes {

    private Long userIdx;
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
        this.userIdx=messageBoard.getUserInfo().getId();
        int size=0;
        if(messageBoard==null||messageBoard.getId()==null){
            System.out.println("nullllllllllll");
            this.isExistent="N";
            this.messageBoardIdx=null;
            this.userNickName=null;
            this.msg= null;
            this.heartNum=null;
            this.date= null;
            this.commentNum = null;
            this.picture= null;

        }
        else{
            this.isExistent="Y";
        }
        this.messageBoardIdx=messageBoard.getId();
        this.userNickName=messageBoard.getUserInfo().getNickname();
        this.msg= messageBoard.getMessage();
        this.heartNum=messageBoard.getHeartNum().toString();
        int index=0;
        index=messageBoard.getAddressMsg().indexOf("구");
        if(index!=-1){
            this.thirdAddressName=messageBoard.getAddressMsg().substring(index+1).trim();

        }
        else{
            this.thirdAddressName=messageBoard.getAddressMsg().trim();
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
