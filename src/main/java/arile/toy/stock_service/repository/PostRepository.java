package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUserUnchangeableIdAndPostId(String unchangeableId, Long postId);
    List<Post> findAllByUserUnchangeableId(String unchangeableId);
    void deleteByUserUnchangeableIdAndPostId(String unchangeableId, Long postId);
}
