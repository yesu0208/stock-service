package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.*;
import arile.toy.stock_service.domain.post.Post;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.dto.PostDto;
import arile.toy.stock_service.dto.ReplyDto;
import arile.toy.stock_service.dto.SimplePostDto;
import arile.toy.stock_service.dto.response.PostResponse;
import arile.toy.stock_service.exception.group.GroupNotFoundException;
import arile.toy.stock_service.exception.post.PostNotFoundException;
import arile.toy.stock_service.repository.DislikeRepository;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.LikeRepository;
import arile.toy.stock_service.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[Service] 게시물 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService sut;

    @Mock
    private PostRepository postRepository;
    @Mock
    private GithubUserInfoRepository githubUserInfoRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private DislikeRepository dislikeRepository;

    @DisplayName("unchangeableId와 PostId가 주어지면, 게시물을 반환한다. (해당 유저의 게시물 좋아요/싫어요 여부도 포함)")
    @Test
    void givenUnchangeableIdAndPostId_whenLoadingExistingPost_thenReturnsPost() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("title", "stockName", "body", 0L, 0L, 0L, githubUserInfo);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        given(likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());
        given(dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());

        // When
        PostResponse result = sut.loadPost(unchangeableId, postId);

        // Then
        assertThat(result)
                .extracting("body")
                .isEqualTo("body");
        then(postRepository).should().findById(postId);
        then(githubUserInfoRepository).should().findById(unchangeableId);
        then(likeRepository).should().findByGithubUserInfoAndPost(githubUserInfo, post);
        then(dislikeRepository).should().findByGithubUserInfoAndPost(githubUserInfo, post);

    }

    @DisplayName("postId에 해당하는 게시물이 없으면, 예외를 던진다.")
    @Test
    void givenUnchangeableIdAndPostId_whenLoadingNonexistingPost_thenThrowsException() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> sut.loadPost(unchangeableId, postId));

        // Then
        assertThat(thrown)
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("Post with postId " + postId + " not found");
    }


    @DisplayName("모든 게시물의 목록을 반환한다.")
    @Test
    void givenNothing_whenLoadingAllPosts_thenReturnsAllPosts() {
        // Given
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        List<Post> posts = List.of(Post.of("title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo));
        given(postRepository.findAll()).willReturn(posts);

        // When
        List<SimplePostDto> result = sut.loadAllSimplePosts();

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting("title")
                .containsExactly("title");
        then(postRepository).should().findAll();
    }


    @DisplayName("내가 작성한 게시물의 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingAllMyPosts_thenReturnsAllMyPosts() {
        // Given
        String unchangeableId = "123456";
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        List<Post> posts = List.of(Post.of("title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo));
        given(postRepository.findAllByUserUnchangeableId(unchangeableId)).willReturn(posts);

        // When
        List<SimplePostDto> result = sut.loadAllMySimplePosts(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting("title")
                .containsExactly("title");
        then(postRepository).should().findAllByUserUnchangeableId(unchangeableId);
    }


    @DisplayName("존재하지 않는 게시물 정보가 주어지면, 게시물을 생성한다.")
    @Test
    void givenNonExistentPost_whenUpserting_thenCreatesPost() {
        // Given
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var postDto = PostDto.of("title", "삼성전자", "body", "test-name", "123456");
        var post = Post.of(1L, "title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo);

        given(githubUserInfoRepository.findById(postDto.unchangeableId())).willReturn(Optional.of(githubUserInfo)).willReturn(Optional.of(githubUserInfo));
        given(postRepository.findByUserUnchangeableIdAndPostId(postDto.unchangeableId(), postDto.postId())).willReturn(Optional.empty());
        given(postRepository.save(any(Post.class))).willReturn(post);
        // When
        sut.upsertPost(postDto, postDto.postId());

        // Then
        then(postRepository).should().save(any(Post.class));

    }


    @DisplayName("존재하는 게시물 정보가 주어지면, 게시물을 수정한다.")
    @Test
    void givenExistentPost_whenUpserting_thenUpdatesPost() {
        // Given
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var postDto = PostDto.of("title", "삼성전자", "body", "test-name", "123456");
        var post = Post.of(1L, "title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo);

        given(githubUserInfoRepository.findById(postDto.unchangeableId())).willReturn(Optional.of(githubUserInfo)).willReturn(Optional.of(githubUserInfo));
        given(postRepository.findByUserUnchangeableIdAndPostId(postDto.unchangeableId(), postDto.postId())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);
        // When
        sut.upsertPost(postDto, postDto.postId());

        // Then
        then(postRepository).should().save(any(Post.class));

    }


//    @DisplayName("unchangeableId와 postId가 주어지면, 게시물을 삭제한다.")
//    @Test
//    void givenUnchangeableIdAndPostId_whenDeleting_thenDeletesPost() {
//        // Given
//        String unchangeableId = "123456";
//        Long postId = 1L;
//        willDoNothing().given(postRepository).deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
//
//        // When
//        sut.deletePost(unchangeableId, postId);
//
//        // Then
//        then(postRepository).should().deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
//    }


    @DisplayName("unchangeableId와 postId가 주어지면, 게시물에 좋아요 버튼을 누른다(or 취소한다).")
    @Test
    void givenUnchangeableIdAndPostId_whenTogglingLikes_thenUpdatesLikesCount() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of(1L, "title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        given(likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());
        given(dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());
        given(postRepository.save(post)).willReturn(post);


        // When
        PostResponse result = sut.toggleLike(postId, unchangeableId);

        // Then
        then(likeRepository).should(times(0)).delete(any(Like.class));
        then(likeRepository).should().save(Like.of(githubUserInfo, post));
        then(dislikeRepository).should(times(0)).delete(any(Dislike.class));

    }


    @DisplayName("unchangeableId와 postId가 주어지면, 게시물에 싫어요 버튼을 누른다(or 취소한다).")
    @Test
    void givenUnchangeableIdAndPostId_whenTogglingDislikes_thenUpdatesDislikesCount() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of(1L, "title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        given(likeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());
        given(dislikeRepository.findByGithubUserInfoAndPost(githubUserInfo, post)).willReturn(Optional.empty());
        given(postRepository.save(post)).willReturn(post);


        // When
        PostResponse result = sut.toggleDislike(postId, unchangeableId);

        // Then
        then(dislikeRepository).should(times(0)).delete(any(Dislike.class));
        then(dislikeRepository).should().save(Dislike.of(githubUserInfo, post));
        then(likeRepository).should(times(0)).delete(any(Like.class));

    }

}