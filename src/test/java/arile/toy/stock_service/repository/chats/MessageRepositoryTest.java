package arile.toy.stock_service.repository.chats;

import arile.toy.stock_service.domain.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;


@DisplayName("[Repository] 메시지 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MessageRepositoryTest {

    @Autowired private MessageRepository sut;


    @DisplayName("chatroomID로 해당 채팅방의 메시지 목록을 반환한다.")
    @Test
    void givenChatroomId_whenSelectingMessageList_thenReturnsMessageList() {
        // Given
        Long chatroomId = 1L;

        // When
        List<Message> result = sut.findAllByChatroomChatroomId(chatroomId);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting("chatroom.chatroomId", "text", "githubUserInfo.unchangeableId")
                .contains(tuple(1L, "메시지 내용 1", "123456"))
                .contains(tuple(1L, "메시지 내용 2", "123456"));
    }

    @DisplayName("chatroomID와 lastCheckedAt(마지막 채팅방 들어간 시각)으로 해당 채팅방에 새 메시지가 있는지 확인한다.")
    @Test
    void givenChatroomIdAndLastCheckedAt_whenCheckingNewMessage_thenReturnsNewMessageExists() {
        // Given
        Long chatroomId = 1L;
        LocalDateTime lastCheckedAt = LocalDateTime.now();

        // When
        Boolean result = sut.existsByChatroomChatroomIdAndCreatedAtAfter(chatroomId, lastCheckedAt);

        // Then
        assertThat(result).isFalse();
    }


}