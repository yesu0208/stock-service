package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.post.Reply;
import arile.toy.stock_service.domain.post.Post;
import arile.toy.stock_service.dto.postdto.ReplyDto;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.post.PostRepository;
import arile.toy.stock_service.repository.post.ReplyRepository;
import arile.toy.stock_service.service.post.ReplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[Service] 댓글 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @InjectMocks
    ReplyService sut;

    @Mock ReplyRepository replyRepository;
    @Mock GithubUserInfoRepository githubUserInfoRepository;
    @Mock PostRepository postRepository;


    @DisplayName("PostId가 주어지면, 해당 게시물의 댓글 목록을 반환한다.")
    @Test
    void givenPostId_whenLoadingReplies_thenReturnsAllPostReplies() {
        // Given
        Long postId = 1L;
        var user = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("제목", "삼성전자", "게시물 내용", 0L, 0L, 0L, user);
        List<Reply> replies = List.of(Reply.of("댓글 내용", user, post));
        given(replyRepository.findAllByPostPostId(postId)).willReturn(replies);

        // When
        List<ReplyDto> result = sut.getAllRepliesByPostId(postId);

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting("body")
                .containsExactly("댓글 내용");
        then(replyRepository).should().findAllByPostPostId(postId);
    }



    @DisplayName("존재하지 않는 댓글 정보가 주어지면, 댓글을 생성한다.")
    @Test
    void givenNonExistentReply_whenUpserting_thenCreatesReply() {
        // Given
        Long postId = 1L;
        Long replyId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("title", "stockName", "body", 0L, 0L, 0L, githubUserInfo);
        var replyDto = ReplyDto.of("body", "test-name", "123456");
        given(githubUserInfoRepository.findById(replyDto.unchangeableId())).willReturn(Optional.of(githubUserInfo));
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(replyRepository.save(any(Reply.class))).willReturn(null);

        // When
        sut.upsertReply(replyDto, postId, replyId);

        // Then
        then(githubUserInfoRepository).should().findById(replyDto.unchangeableId());
        then(postRepository).should().findById(postId);
        then(replyRepository).should().save(any(Reply.class));

    }


    @DisplayName("존재하는 댓글 정보가 주어지면, 댓글을 수정한다.")
    @Test
    void givenExistentReply_whenUpserting_thenUpdatesReply() {
        // Given
        Long postId = 1L;
        Long replyId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("title", "stockName", "body", 0L, 0L, 0L, githubUserInfo);
        var replyDto = ReplyDto.of("body", "test-name", "123456");
        given(githubUserInfoRepository.findById(replyDto.unchangeableId())).willReturn(Optional.of(githubUserInfo));
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(replyRepository.save(any(Reply.class))).willReturn(null);

        // When
        sut.upsertReply(replyDto, postId, replyId);

        // Then
        then(githubUserInfoRepository).should().findById(replyDto.unchangeableId());
        then(postRepository).should().findById(postId);
        then(replyRepository).should().save(any(Reply.class));

    }

    @DisplayName("unchangeableId와 postId, replyId가 주어지면, 댓글을 삭제한다.")
    @Test
    void givenUnchangeableIdAndPostIdAndReplyId_whenDeleting_thenDeletesReply() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        Long replyId = 1L;
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("title", "stockName", "body", 0L, 0L, 0L, githubUserInfo);
        var reply = Reply.of("body", githubUserInfo, post);
        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));
        willDoNothing().given(replyRepository).deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, replyId);

        // When
        sut.deleteReply(unchangeableId, postId, replyId);

        // Then
        then(replyRepository).should().deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, replyId);
    }
}