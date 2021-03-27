package ga.todayOutside.src.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Getter
@NoArgsConstructor
public class GetCommentRes {

    private Long userIdx;
    private Long commentIdx;
    private String thirdAddressName;
    private String userNickName;
    private String date;
    private String msg;
    private String picture;

    public GetCommentRes(Comment comment){
        this.userIdx=comment.getUserInfo().getId();
        this.commentIdx=comment.getId();
        this.picture=comment.getUserInfo().getPicture();
        this.msg=comment.getCommentMsg();
        this.userNickName=comment.getUserInfo().getNickname();
        String[]array=comment.getAddressMsg().split(" ");
        if(array.length>1){
            this.thirdAddressName=array[(array.length)-1];
        }
        else{
            this.thirdAddressName=getThirdAddressName();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date= simpleDateFormat.format(comment.getCreatedAt()); ;




    }

}
