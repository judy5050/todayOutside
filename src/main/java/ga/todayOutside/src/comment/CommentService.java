package ga.todayOutside.src.comment;



import ga.todayOutside.config.BaseException;
import ga.todayOutside.config.BaseResponseStatus;
import ga.todayOutside.src.comment.model.Comment;
import ga.todayOutside.src.comment.model.GetCommentRes;
import ga.todayOutside.src.messageBoard.models.GetMessageBoardRecentlyRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private  final CommentRepository commentRepository;

    public List<GetCommentRes> findAllByMessageId(Long messageBoardIdx,String page) throws BaseException {

        PageRequest pageRequest=PageRequest.of(Integer.parseInt(page),10);
        Page<Comment> comments = commentRepository.findAllByMessageId(messageBoardIdx, pageRequest);
        List<GetCommentRes> getCommentRes=comments.map(GetCommentRes::new).getContent();
        if(getCommentRes.isEmpty()){
            throw new BaseException(BaseResponseStatus.NOU_FOUND_COMMENT);
        }

        return getCommentRes;
    }
}
