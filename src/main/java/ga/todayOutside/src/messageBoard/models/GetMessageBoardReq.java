package ga.todayOutside.src.messageBoard.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
public class GetMessageBoardReq {


    private Long userIdx;
    private String picture;
    private String userNickName;
    private String thirdAddressName;
    private String msg;
    private String heartNum;
    private String commentNum; //댓글 수
    private String date;



    public GetMessageBoardReq(MessageBoard messageBoard) {

        this.userIdx=messageBoard.getUserInfo().getId();
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
        this.date= simpleDateFormat.format(messageBoard.getCreatedAt()); ;
        this.commentNum=Integer.toString(messageBoard.getComments().size());
        this.picture= messageBoard.getUserInfo().getPicture();



    }


}
