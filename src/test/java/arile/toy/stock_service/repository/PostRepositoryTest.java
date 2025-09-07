package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Post;
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

@DisplayName("[Repository] 게시물 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PostRepositoryTest {

    @Autowired private PostRepository sut;

    @DisplayName("unchangeableId와 postId로 게시물을 반환한다.")
    @Test
    void givenUnchangeableIdAndPostId_whenSelectingPost_thenReturnsPost() {
        // Given
        String unchangeableId = "123456";
        Long postId = 2L;

        // When
        Optional<Post> result = sut.findByUserUnchangeableIdAndPostId(unchangeableId, postId);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("user.unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("postId", postId)
                .hasFieldOrPropertyWithValue("stockName", "SK하이닉스");
    }


    @DisplayName("unchangeableId로 유저가 쓴 게시물 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenSelectingInterestGroupList_thenReturnsInterestGroupList() {
        // Given
        String unchangeableId = "123456";

        // When
        List<Post> result = sut.findAllByUserUnchangeableId(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting("user.unchangeableId", "stockName")
                .contains(tuple("123456", "삼성전자"));
    }


    @DisplayName("unchangeableId와 postId로 게시물을 삭제한다.")
    @Test
    void givenUnchangeableIdAndPostId_whenDeletingPost_thenDeletesPost() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;
        
        // When
        sut.deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);

        // Then
        assertThat(sut.findByUserUnchangeableIdAndPostId(unchangeableId, postId)).isEmpty();
    }

}