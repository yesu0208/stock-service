package arile.toy.stock_service.service.post;

import arile.toy.stock_service.repository.post.DislikeRepository;
import arile.toy.stock_service.repository.post.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeDislikeService {

    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;

    public Boolean doesUserLikePost(String unchangeableId, Long postId) {
        return likeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId).isPresent();
    }

    public Boolean doesUserDislikePost(String unchangeableId, Long postId) {
        return dislikeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId).isPresent();
    }
}
