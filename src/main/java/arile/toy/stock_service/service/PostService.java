package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.post.Dislike;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.post.Like;
import arile.toy.stock_service.dto.postdto.PostDto;
import arile.toy.stock_service.dto.postdto.SimplePostDto;
import arile.toy.stock_service.exception.post.PostNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.post.DislikeRepository;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.post.LikeRepository;
import arile.toy.stock_service.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final GithubUserInfoRepository githubUserInfoRepository;
    private final LikeDislikeService likeDislikeService;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;

    @Transactional(readOnly = true)
    public PostDto loadPost(String unchangeableId, Long postId) {

        Boolean isLiking = likeDislikeService.doesUserLikePost(unchangeableId, postId);

        Boolean isDisliking = likeDislikeService.doesUserDislikePost(unchangeableId, postId);

        return postRepository.findById(postId)
                .map(entity -> PostDto.fromEntity(entity, isLiking, isDisliking))
                .orElseThrow(() -> new PostNotFoundException(postId));

    }


    @Transactional(readOnly = true)
    public List<SimplePostDto> loadAllSimplePosts() {
        return postRepository.findAll()
                .stream()
                .map(SimplePostDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SimplePostDto> loadAllMySimplePosts(String unchangeableId) {
        return postRepository.findAllByUserUnchangeableId(unchangeableId)
                .stream()
                .map(SimplePostDto::fromEntity)
                .toList();
    }


    public Long upsertPost(PostDto dto, Long postId){

        GithubUserInfo user = githubUserInfoRepository.findById(dto.unchangeableId())
                .orElseThrow(() -> new UserNotFoundException(dto.unchangeableId()));

        PostDto savedPostDto = PostDto.fromEntity(postRepository.findByUserUnchangeableIdAndPostId(dto.unchangeableId(), postId)
                .map(entity -> postRepository.save(dto.updateEntity(entity)))  // 기존 글이면 update
                .orElseGet(() -> postRepository.save(dto.createEntity(user)))); // 새 글이면 create

        // upsertPostId
        return savedPostDto.postId();  // PK 값 가져오기

    }

    public void deletePost(String unchangeableId, Long postId) {

        postRepository.deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
    }


    public PostDto toggleLike(String unchangeableId, Long postId) {
        var post = postRepository.findById(postId) // Optional<PostEntity>
                .orElseThrow(() -> new PostNotFoundException(postId));

        var githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        var like = likeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId); // 좋아요를 눌렀거나, 안눌렀거나(Optional)

        var dislike = dislikeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId);

        if (like.isPresent()) { // 이미 존재하면, 삭제
            likeRepository.delete(like.get());
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            return PostDto.fromEntity(postRepository.save(post), false, false);
        } else { // 존재하지 않는다면, 추가
            likeRepository.save(Like.of(githubUserInfo, post));
            if (dislike.isPresent()) { // 만약, 싫어요를 눌렀다면 싫어요를 취소하고 좋아요로 변환
                dislikeRepository.delete(dislike.get());
                post.setDislikesCount(post.getDislikesCount() - 1);
            }
            post.setLikesCount(post.getLikesCount() + 1);
            return PostDto.fromEntity(postRepository.save(post), true, false);
        }
    }


    public PostDto toggleDislike(String unchangeableId, Long postId) {
        var post = postRepository.findById(postId) // Optional<PostEntity>
                .orElseThrow(() -> new PostNotFoundException(postId));

        var githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        var like = likeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId); // 좋아요를 눌렀거나, 안눌렀거나(Optional)

        var dislike = dislikeRepository.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId);

        if (dislike.isPresent()) { // 이미 존재하면, 삭제
            dislikeRepository.delete(dislike.get());
            post.setDislikesCount(Math.max(0, post.getDislikesCount() - 1));
            return (PostDto.fromEntity(postRepository.save(post), false, false));
        } else { // 존재하지 않는다면, 추가
            dislikeRepository.save(Dislike.of(githubUserInfo, post));
            if (like.isPresent()) { // 만약, 싫어요를 눌렀다면 싫어요를 취소하고 좋아요로 변환
                likeRepository.delete(like.get());
                post.setLikesCount(post.getLikesCount() - 1);
            }
            post.setDislikesCount(post.getDislikesCount() + 1);
            return PostDto.fromEntity(postRepository.save(post), false, true);
        }
    }


}
