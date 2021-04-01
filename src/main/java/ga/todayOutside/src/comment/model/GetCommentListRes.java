package ga.todayOutside.src.comment.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentListRes {
    private List<GetCommentRes> commentList;
    private Long totalCount;

    public GetCommentListRes(List<GetCommentRes> getCommentRes, long totalElements) {
        this.commentList=getCommentRes;
        this.totalCount=totalElements;
    }
}
