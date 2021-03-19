package ga.todayOutside.src.messageBoard;


import ga.todayOutside.src.messageBoard.models.MessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageBoardRepository extends JpaRepository<MessageBoard,Long> {






}
