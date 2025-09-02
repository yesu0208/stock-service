package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;

@DisplayName("[Repository] 댓글 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ReplyRepositoryTest {

    @Autowired ReplyRepository sut;

    @DisplayName("unchangeableId와 postId, replyId로 댓글을 반환한다.")
    @Test
    void givenUnchangeableIdAndPostIdAndReplyId_whenSelectingReply_thenReturnsReply() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        Long replyId = 1L;

        // When
        Optional<Reply> result = sut.findByReplyIdAndUserUnchangeableIdAndPostPostId(replyId, unchangeableId, postId);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("body", "내용 1")
                .hasFieldOrPropertyWithValue("post.postId", postId)
                .hasFieldOrPropertyWithValue("user.unchangeableId", "123456");
    }


    @DisplayName("postId로 게시물의 댓글 목록을 반환한다.")
    @Test
    void givenPostId_whenSelectingReplyList_thenReturnsReplyList() {
        // Given
        Long postId = 1L;

        // When
        List<Reply> result = sut.findAllByPostPostId(postId);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting("user.unchangeableId", "body")
                .contains(tuple("123456", "내용 1"));
    }


    @DisplayName("unchangeableId와 postId, replyId로 댓글을 삭제한다.")
    @Test
    void givenUnchangeableIdAndPostIdAndReplyId_whenDeletingReply_thenDeletesReply() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        Long replyId = 1L;

        // When
        sut.deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, replyId);

        // Then
        assertThat(sut.findByReplyIdAndUserUnchangeableIdAndPostPostId(replyId, unchangeableId, postId)).isEmpty();
    }

}