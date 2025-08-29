package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.Reply;
import arile.toy.stock_service.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByReplyIdAndUserUnchangeableIdAndPostPostId(Long replyId, String unchangeableId, Long postId);
    List<Reply> findAllByPostPostId(Long postId);
    void deleteByUserUnchangeableIdAndPostPostIdAndReplyId(String unchangeableId, Long postId, Long replyId);
}
