package ga.todayOutside.src.heartHistory;


import ga.todayOutside.config.BaseException;
import ga.todayOutside.src.heartHistory.model.HeartHistory;
import ga.todayOutside.src.heartHistory.model.HeartStatus;
import ga.todayOutside.src.messageBoard.MessageBoardService;
import ga.todayOutside.src.messageBoard.models.MessageBoard;
import ga.todayOutside.src.user.UserInfoService;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartHistoryService {

    private  final HeartHistoryRepository heartHistoryRepository;
    private final MessageBoardService messageBoardService;
    private final UserInfoService userInfoService;

    @Transactional
    public HeartHistory postHeart(MessageBoard messageBoard, UserInfo userInfo) throws BaseException {

        HeartHistory heartHistory = heartHistoryRepository.findByUserIdxAndMessageIdx(userInfo.getId(), messageBoard.getId()).orElse(null);
        HeartHistory heartHistory1=null;
        System.out.println("heartHistory = " + heartHistory);
        //처음 누른 경우
        if(heartHistory==null){


             heartHistory1=new HeartHistory(userInfo,messageBoard,HeartStatus.Y);
            heartHistoryRepository.save(heartHistory1);
            messageBoardService.updateHeartNumPlus(heartHistory1.getMessageBoard().getId());

            //messageBoard 를 작성한 유저를 찾는다.
            UserInfo userInfo1 = messageBoardService.findByUser(messageBoard);

            //유저를 찾아, 해당 유저의 heartNum 수 증가
            userInfoService.updateUserHeartPlus(userInfo1);

        }
        //하트 눌리기 전
        else if(heartHistory.getHeartStatus().toString().equals("N")){


            messageBoardService.updateHeartNumPlus(messageBoard.getId());
             heartHistory1  = heartHistoryRepository.findById(heartHistory.getId()).orElse(null);
            heartHistory1.setHeartStatus(HeartStatus.Y);
            heartHistoryRepository.save(heartHistory1);

            //messageBoard 를 작성한 유저를 찾는다.
            UserInfo userInfo1 = messageBoardService.findByUser(messageBoard);

            //유저를 찾아, 해당 유저의 heartNum 수 증가
            userInfoService.updateUserHeartPlus(userInfo1);


        }
        //하트 눌린 후 (취소)
        else if(heartHistory.getHeartStatus().toString().equals("Y")){


            messageBoardService.updateHeartNumSub(messageBoard.getId());
            //위에서 영속성 컨텍스트에 있는것들을 싹다 비우기 때문에 밑에서 다시 영속성 컨텍스트에 heart1 을 올림
             heartHistory1  = heartHistoryRepository.findById(heartHistory.getId()).orElse(null);
            heartHistory1.setHeartStatus(HeartStatus.N);
            heartHistoryRepository.save(heartHistory1);

            //messageBoard 를 작성한 유저를 찾는다.
            UserInfo userInfo1 = messageBoardService.findByUser(messageBoard);

            //유저를 찾아, 해당 유저의 heartNum 수 증가
            userInfoService.updateUserHeartSub(userInfo1);


        }

        return heartHistory1;

    }
}
