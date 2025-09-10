package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.post.Dislike;
import arile.toy.stock_service.repository.post.DislikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Repository] 싫어요 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DislikeRepositoryTest {

    @Autowired private DislikeRepository sut;

    @DisplayName("unchangeableId와 postId로 싫어요 정보를 반환한다.")
    @Test
    void givenUnchangeableIdAndPostId_whenSelectingDislike_thenReturnsDislike() {
        // Given
        String unchangeableId = "123456";
        Long postId = 1L;

        // When
        Optional<Dislike> result = sut.findByGithubUserInfoUnchangeableIdAndPostPostId(unchangeableId, postId);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("githubUserInfo.unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("post.postId", postId);
    }

}