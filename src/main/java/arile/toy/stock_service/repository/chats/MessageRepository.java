package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatroomChatroomId(Long chatroomId);
}
