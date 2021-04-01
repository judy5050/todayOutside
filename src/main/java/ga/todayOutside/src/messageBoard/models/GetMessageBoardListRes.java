package ga.todayOutside.src.messageBoard.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class GetMessageBoardListRes {

    private List<GetMessageBoardRecentlyRes> messageList;
    private Long totalCount;


    public GetMessageBoardListRes(List<GetMessageBoardRecentlyRes> getMessageBoardHeartRes1, long totalElements) {

            this.messageList=getMessageBoardHeartRes1;
            this.totalCount=totalElements;
         
        }


}
