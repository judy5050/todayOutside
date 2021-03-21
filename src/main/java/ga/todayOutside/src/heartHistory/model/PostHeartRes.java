package ga.todayOutside.src.heartHistory.model;

import ga.todayOutside.src.messageBoard.models.MessageBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostHeartRes {

    private Long heartNum;

    public PostHeartRes(MessageBoard messageBoard) {
        this.heartNum= messageBoard.getHeartNum();
    }
}
