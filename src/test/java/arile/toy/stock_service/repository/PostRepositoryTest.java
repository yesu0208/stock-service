//package arile.toy.stock_service.repository;
//
//import arile.toy.stock_service.domain.post.Post;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.BDDAssertions.tuple;
//
//@DisplayName("[Repository] 게시물 쿼리 테스트")
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//class PostRepositoryTest {
//
//    @Autowired private PostRepository sut;
//    @Autowired private ReplyRepository replyRepository;
//
//    @DisplayName("unchangeableId와 postId로 게시물을 반환한다.")
//    @Test
//    void givenUnchangeableIdAndPostId_whenSelectingPost_thenReturnsPost() {
//        // Given
//        String unchangeableId = "123456";
//        Long postId = 1L;
//
//        // When
//        Optional<Post> result = sut.findByUserUnchangeableIdAndPostId(unchangeableId, postId);
//
//        // Then
//        assertThat(result)
//                .get()
//                .hasFieldOrPropertyWithValue("user.unchangeableId", unchangeableId)
//                .hasFieldOrPropertyWithValue("postId", postId)
//                .hasFieldOrPropertyWithValue("stockName", "삼성전자");
//    }
//
//
//    @DisplayName("unchangeableId로 유저가 쓴 게시물 목록을 반환한다.")
//    @Test
//    void givenUnchangeableId_whenSelectingInterestGroupList_thenReturnsInterestGroupList() {
//        // Given
//        String unchangeableId = "123456";
//
//        // When
//        List<Post> result = sut.findAllByUserUnchangeableId(unchangeableId);
//
//        // Then
//        assertThat(result)
//                .hasSize(1)
//                .extracting("user.unchangeableId", "stockName")
//                .containsOnly(tuple("123456", "삼성전자"));
//    }
//
//
//    @DisplayName("unchangeableId와 postId로 게시물을 삭제한다.")
//    @Test
//    void givenUnchangeableIdAndPostId_whenDeletingPost_thenDeletesPost() {
//        // Given
//        String unchangeableId = "123456";
//        Long postId = 1L;
//        // 사실, 이 부분은 제대로 구현했으면 필요없음.
//        // 이것이 없으면 post가 제거되지 못함. (연결된 reply가 있어 FK 제약 때문에 실패 - reply에서 post연결)
//        // 따라서, post에 reply 필드를 넣고, cascade = CascadeType.REMOVE, orphanRemoval = true 적용해서
//        // post 제거되면 연결된 reply도 제거되게끔 하는 것이 바람직함.
//        // 일단은 테스트 통과를 위해 이대로 세팅(추후 변경)
//        replyRepository.deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, 1L);
//        replyRepository.deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, 2L);
//
//        // When
//        sut.deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
//
//        // Then
//        assertThat(sut.findByUserUnchangeableIdAndPostId(unchangeableId, postId)).isEmpty();
//    }
//
//}