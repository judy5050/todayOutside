package ga.todayOutside.src.messageBoard.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;


@Getter
@NoArgsConstructor
public class GetMyMessageListRes {
    private Long messageBoardIdx;
    private String userNickName;
    private String userPicture;
    private String  commentNum;
    private String heartNum;
    private String thirdAddressName;
    private String date;
    private String msg;

    public GetMyMessageListRes(MessageBoard messageBoard){
        this.messageBoardIdx=messageBoard.getId();
        this.userNickName=messageBoard.getUserInfo().getNickname();
        this.userPicture=messageBoard.getUserInfo().getPicture();
        this.commentNum=Integer.toString((int) messageBoard.getComments().stream().count());

        int index;
        //게시글 필터링 구 정보 받기
        if(messageBoard.getAddressMsg().matches(".* .*")){
            index=messageBoard.getAddressMsg().indexOf(" ");
            this.thirdAddressName=messageBoard.getAddressMsg().substring(index+1);
        }
        //시 구 두개 다 합쳐있을경우 구 정보만 받기
        else{
            this.thirdAddressName=messageBoard.getAddressMsg();

        }
        this.heartNum=messageBoard.getHeartNum().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date= simpleDateFormat.format(messageBoard.getCreatedAt());
        this.msg=messageBoard.getMessage();

    }
}
