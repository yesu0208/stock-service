package arile.toy.stock_service.repository.stockchats;

import arile.toy.stock_service.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
