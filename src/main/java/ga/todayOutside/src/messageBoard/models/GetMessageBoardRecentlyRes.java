package ga.todayOutside.src.messageBoard.models;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;


@Getter
@NoArgsConstructor
public class GetMessageBoardRecentlyRes {
    private String userNickName;
    private String thirdAddressName;
    private String msg;
    private Long heartNum;
    private Long commentNum; //댓글 수
    private String date;
}
