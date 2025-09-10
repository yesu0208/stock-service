package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    Optional<Dislike> findByGithubUserInfoUnchangeableIdAndPostPostId(String unchangeableId, Long postId);
}
