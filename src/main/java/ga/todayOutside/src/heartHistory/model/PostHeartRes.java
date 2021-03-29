package ga.todayOutside.src.heartHistory.model;

import ga.todayOutside.src.messageBoard.models.MessageBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostHeartRes {

    private String status;


    public PostHeartRes(HeartHistory heartHistory) {
        this.status=heartHistory.getHeartStatus().toString();
    }
}
