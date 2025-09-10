package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.chat.GithubUserChatroomMapping;
import arile.toy.stock_service.repository.chat.GithubUserChatroomMappingRepository;
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


@DisplayName("[Repository] 유저-채팅방 메핑 테이블 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GithubUserChatroomMappingRepositoryTest {

    @Autowired private GithubUserChatroomMappingRepository sut;

    @DisplayName("unchangeableID와 chatroomId으로 유저-채팅방 메핑이 있는지 확인한다.")
    @Test
    void givenUnchangeableIdAndChatroomIdA_whenCheckingGithubUserChatroomMapping_thenReturnsGithubUserChatroomMappingExists() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;

        // When
        Boolean result = sut.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);

        // Then
        assertThat(result).isTrue();
    }

    @DisplayName("unchangeableID와 chatroomId으로 유저-채팅방 메핑을 삭제한다.")
    @Test
    void givenUnchangeableIdAndChatroomIdA_whenDeletingGithubUserChatroomMapping_thenDeletesGithubUserChatroomMapping() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;

        // When
        sut.deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);

        // Then
        assertThat(sut.findByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId)).isEmpty();
    }

    @DisplayName("unchangeableId로 유저-채팅방 메핑 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenSelectingGithubUserChatroomMappingList_thenReturnsSelectingGithubUserChatroomMappingList() {
        // Given
        String unchangeableId = "123456";

        // When
        List<GithubUserChatroomMapping> result = sut.findAllByGithubUserInfoUnchangeableId(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting("githubUserInfo.unchangeableId", "chatroom.chatroomId")
                .containsOnly(tuple("123456", 1L));
    }

    @DisplayName("unchangeableId와 chatroomId로 유저-채팅방 매핑 정보를 반환한다.")
    @Test
    void givenUnchangeableIdAndChatroomId_whenSelectingGithubUserChatroomMapping_thenReturnsGithubUserChatroomMapping() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;

        // When
        Optional<GithubUserChatroomMapping> result =
                sut.findByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("githubUserInfo.unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("chatroom.chatroomId", chatroomId);
    }

}