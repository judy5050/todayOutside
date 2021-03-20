package ga.todayOutside.src.messageBoard.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
public class GetMessageBoardReq {


    private String picture;
    private String userNickName;
    private String thirdAddressName;
    private String msg;
    private Long heartNum;
    private int commentNum; //댓글 수
    private String date;



    public GetMessageBoardReq(MessageBoard messageBoard) {

        this.userNickName=messageBoard.getUserInfo().getNickname();
        this.msg= messageBoard.getMessage();
        this.heartNum=messageBoard.getHeartNum();

        String[]array=messageBoard.getAddressMsg().split(" ");
        if(array.length>1){
            this.thirdAddressName=array[(array.length)-1];
        }
        else{
            this.thirdAddressName=getThirdAddressName();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date= simpleDateFormat.format(messageBoard.getCreatedAt()); ;
        this.commentNum=messageBoard.getComments().size();
        this.picture= messageBoard.getUserInfo().getPicture();



    }


}
