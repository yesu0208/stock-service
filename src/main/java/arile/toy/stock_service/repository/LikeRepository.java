package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Like;
import arile.toy.stock_service.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByGithubUserInfoUnchangeableIdAndPostPostId(String unchangeableId, Long postId);
    void deleteAllByPostPostId(Long postId);
}
