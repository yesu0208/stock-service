package arile.toy.stock_service.repository;


import arile.toy.stock_service.domain.Dislike;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Like;
import arile.toy.stock_service.domain.post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Repository] 좋아요 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository sut;

    @Autowired private GithubUserInfoRepository githubUserInfoRepository;
    @Autowired private PostRepository postRepository;

    @DisplayName("유저 정보와 게시물 정보로 좋아요 정보를 반환한다.")
    @Test
    void givenGithubUserInfoAndPost_whenSelectingLike_thenReturnsLike() {
        // Given
        var githubUserInfo = GithubUserInfo.of("123456", "test-id", "test-name", "test@email.com", 0.1);
        var post = Post.of("title", "삼성전자", "body", 0L, 0L, 0L, githubUserInfo);
        // 여기서 1L 같은 id를 직접 넣으면, JPA 입장에서는
        // "이미 DB에 존재해야 하는 Post 엔티티(=detached 객체)"라고 인식함..
        //그런데 실제 DB에는 없는 id=1L row → merge 시도 → StaleObjectStateException (Optimistic Locking 실패) 발생.

        githubUserInfoRepository.save(githubUserInfo);
        postRepository.save(post);
        var schema = new Like(githubUserInfo, post);
        sut.save(schema);

        // When
        Optional<Like> result = sut.findByGithubUserInfoAndPost(githubUserInfo, post);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("githubUserInfo", githubUserInfo)
                .hasFieldOrPropertyWithValue("post", post);

    }

}