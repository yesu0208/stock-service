package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.Dislike;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Like;
import arile.toy.stock_service.dto.PostDto;
import arile.toy.stock_service.dto.ReplyDto;
import arile.toy.stock_service.dto.SimplePostDto;
import arile.toy.stock_service.dto.response.PostResponse;
import arile.toy.stock_service.exception.post.PostNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.DislikeRepository;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.LikeRepository;
import arile.toy.stock_service.repository.PostRepository;
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
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final ReplyService replyService;

    @Transactional(readOnly = true)
    public PostResponse loadPost(String unchangeableId, Long postId) {
        var post = postRepository.findById(postId)
                // Optional
                .orElseThrow(() -> new PostNotFoundException(postId));

        var githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                // Optional
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        Boolean isLiking = likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post).isPresent();

        Boolean isDisliking = dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post).isPresent();

        return PostResponse.fromDto(PostDto.fromEntity(post), isLiking, isDisliking);
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

        // post의 reply 삭제
        List<Long> replyIdList = replyService.loadAllRepliesByPostId(postId)
                .stream()
                .map(ReplyDto::replyId)
                .toList();
        replyIdList.forEach(replyId -> replyService.deleteReply(unchangeableId, postId, replyId));

        // post의 좋아요, 싫어요 삭제
        likeRepository.deleteAllByPostPostId(postId);
        dislikeRepository.deleteAllByPostPostId(postId);

        postRepository.deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
    }


    public PostResponse toggleLike(Long postId, String unchangeableId) {
        var post = postRepository.findById(postId) // Optional<PostEntity>
                .orElseThrow(() -> new PostNotFoundException(postId));

        var githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        var like = likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post); // 좋아요를 눌렀거나, 안눌렀거나(Optional)

        var dislike = dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post);

        if (like.isPresent()) { // 이미 존재하면, 삭제
            likeRepository.delete(like.get());
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            return PostResponse.fromDto(PostDto.fromEntity(postRepository.save(post)), false, false);
        } else { // 존재하지 않는다면, 추가
            likeRepository.save(Like.of(githubUserInfo, post));
            if (dislike.isPresent()) { // 만약, 싫어요를 눌렀다면 싫어요를 취소하고 좋아요로 변환
                dislikeRepository.delete(dislike.get());
                post.setDislikesCount(post.getDislikesCount() - 1);
            }
            post.setLikesCount(post.getLikesCount() + 1);
            return PostResponse.fromDto(PostDto.fromEntity(postRepository.save(post)), true, false);
        }
    }


    public PostResponse toggleDislike(Long postId, String unchangeableId) {
        var post = postRepository.findById(postId) // Optional<PostEntity>
                .orElseThrow(() -> new PostNotFoundException(postId));

        var githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        var like = likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post); // 좋아요를 눌렀거나, 안눌렀거나(Optional)

        var dislike = dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post);

        if (dislike.isPresent()) { // 이미 존재하면, 삭제
            dislikeRepository.delete(dislike.get());
            post.setDislikesCount(Math.max(0, post.getDislikesCount() - 1));
            return PostResponse.fromDto(PostDto.fromEntity(postRepository.save(post)), false, false);
        } else { // 존재하지 않는다면, 추가
            dislikeRepository.save(Dislike.of(githubUserInfo, post));
            if (like.isPresent()) { // 만약, 싫어요를 눌렀다면 싫어요를 취소하고 좋아요로 변환
                likeRepository.delete(like.get());
                post.setLikesCount(post.getLikesCount() - 1);
            }
            post.setDislikesCount(post.getDislikesCount() + 1);
            return PostResponse.fromDto(PostDto.fromEntity(postRepository.save(post)), false, true);
        }
    }


}
