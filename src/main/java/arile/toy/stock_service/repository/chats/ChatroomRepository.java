package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.chat.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
