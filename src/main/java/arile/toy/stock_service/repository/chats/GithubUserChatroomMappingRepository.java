package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.chat.GithubUserChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GithubUserChatroomMappingRepository extends JpaRepository<GithubUserChatroomMapping, Long> {
    Boolean existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(String unchangeableId, Long chatroomId);
    void deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(String unchangeableId, Long chatroomId);
    List<GithubUserChatroomMapping> findAllByGithubUserInfoUnchangeableId(String unchangeableId);
    Optional<GithubUserChatroomMapping> findByGithubUserInfoUnchangeableIdAndChatroomChatroomId(String unchangeableId, Long chatroomId);
}
