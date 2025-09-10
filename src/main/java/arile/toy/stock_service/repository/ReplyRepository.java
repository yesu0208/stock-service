package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByUserUnchangeableIdAndPostPostIdAndReplyId(String unchangeableId, Long postId, Long replyId);
    List<Reply> findAllByPostPostId(Long postId);
    void deleteByUserUnchangeableIdAndPostPostIdAndReplyId(String unchangeableId, Long postId, Long replyId);
}
