package ga.todayOutside.src.messageBoard.models;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostMessageBoardReq {

    private String msg;
    private BoardType boardType;
}
