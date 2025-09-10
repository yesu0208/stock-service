package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByGithubUserInfoUnchangeableIdAndPostPostId(String unchangeableId, Long postId);
}
