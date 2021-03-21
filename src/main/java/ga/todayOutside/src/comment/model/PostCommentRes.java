package ga.todayOutside.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCommentRes {
    private Long userIdx;
    private Long boardIdx;
    private String  commentMsg;
}
