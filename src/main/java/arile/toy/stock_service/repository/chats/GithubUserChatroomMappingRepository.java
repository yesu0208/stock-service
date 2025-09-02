package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.GithubUserChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserChatroomMappingRepository extends JpaRepository<GithubUserChatroomMapping, Long> {
}
