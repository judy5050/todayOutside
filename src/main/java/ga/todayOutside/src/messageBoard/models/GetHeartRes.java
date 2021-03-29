package ga.todayOutside.src.messageBoard.models;


import ga.todayOutside.src.heartHistory.model.HeartHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetHeartRes {

    private String status;

    public GetHeartRes(HeartHistory heartHistory) {
        this.status=heartHistory.getHeartStatus().toString();
    }

    public GetHeartRes(String n) {
        this.status=n;
    }
}
