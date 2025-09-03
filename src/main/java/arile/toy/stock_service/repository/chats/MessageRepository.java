package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
