package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.GithubUserChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GithubUserChatroomMappingRepository extends JpaRepository<GithubUserChatroomMapping, Long> {
    Boolean existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(String unchangeableId, Long chatroomId);
    void deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(String unchangeableId, Long chatroomId);
    List<GithubUserChatroomMapping> findAllByGithubUserInfoUnchangeableId(String unchangeableId);
}
