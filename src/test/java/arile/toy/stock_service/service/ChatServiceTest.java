package arile.toy.stock_service.service;


import arile.toy.stock_service.domain.chat.Chatroom;
import arile.toy.stock_service.domain.chat.GithubUserChatroomMapping;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.chat.Message;
import arile.toy.stock_service.dto.chatdto.ChatroomDto;
import arile.toy.stock_service.dto.chatdto.ChatroomWithCurrentStockDto;
import arile.toy.stock_service.dto.CurrentStockInfoDto;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.chat.ChatroomRepository;
import arile.toy.stock_service.repository.chat.GithubUserChatroomMappingRepository;
import arile.toy.stock_service.repository.chat.MessageRepository;
import arile.toy.stock_service.service.chat.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@DisplayName("[Service] 종목톡(채팅) 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks private ChatService sut;

    @Mock private ChatroomRepository chatroomRepository;
    @Mock private GithubUserChatroomMappingRepository githubUserChatroomMappingRepository;
    @Mock private MessageRepository messageRepository;
    @Mock private GithubUserInfoRepository githubUserInfoRepository;
    @Mock private CurrentStockInfoService currentStockInfoService;


    @DisplayName("새로운 채팅방 정보가 주어지면, 채팅방을 생성한다.")
    @Test
    void givenNewChatroomInformation_whenCreating_thenCreatesChatroom() {
        // Given
        String unchangeableId = "123456";
        String newTitle = "채팅방 제목";
        String stockName = "삼성전자";
        String createdBy = "test-name";
        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
        var savedChatroom = Chatroom.of(1L, newTitle, new HashSet<>(), LocalDateTime.now(ZoneId.of("Asia/Seoul")), stockName, createdBy, unchangeableId);
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        given(chatroomRepository.save(any(Chatroom.class))).willReturn(savedChatroom);
        given(githubUserChatroomMappingRepository.save(any(GithubUserChatroomMapping.class))).willReturn(null);

        // When
        ChatroomDto result = sut.createChatroom(unchangeableId, newTitle, stockName, createdBy);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("stockName", "삼성전자")
                .hasFieldOrPropertyWithValue("title", "채팅방 제목");
        then(githubUserInfoRepository).should().findById(unchangeableId);
        then(chatroomRepository).should().save(any(Chatroom.class));
        then(githubUserChatroomMappingRepository).should().save(any(GithubUserChatroomMapping.class));
    }


    @DisplayName("기존에 참여한 다른 사람이 만든 채팅빙에 참여하면, 채팅방에 참여하고 false를 반환한다.")
    @Test
    void givenUnchangeableIdAndChatroomId_whenJoiningChatroomNotJoinedBefore_thenJoinsChatroomAndReturnsFalse() {
        // Given
        String unchangeableId = "123456";
        Long currentChatroomId = null;
        Long newChatroomId = 1L;
        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        given(githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, newChatroomId)).willReturn(true);

        // When
        Boolean result = sut.joinChatroom(unchangeableId, newChatroomId, currentChatroomId);

        // Then
        assertThat(result).isFalse();
        then(githubUserInfoRepository).should().findById(unchangeableId);
        then(githubUserChatroomMappingRepository).should().existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, newChatroomId);
        then(chatroomRepository).shouldHaveNoInteractions();
        then(githubUserChatroomMappingRepository).should(never()).save(any(GithubUserChatroomMapping.class));
    }


    @DisplayName("chatroomId가 주어지면, 해당 채팅방을 반환한다.")
    @Test
    void givenChatroomId_whenLoadingChatroom_thenReturnsChatroom() {
        // Given
        Long chatroomId = 1L;
        String stockName = "삼성전자";
        var chatroom = Chatroom.of(1L, "채팅방 제목", new HashSet<>(), LocalDateTime.now(ZoneId.of("Asia/Seoul")), "삼성전자", "test-name", "123456");
        var currentStockInfoDto = CurrentStockInfoDto.of("123456", "삼성전자", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        given(chatroomRepository.findById(chatroomId)).willReturn(Optional.of(chatroom));
        given(currentStockInfoService.getCurrentStockInfo(stockName)).willReturn(currentStockInfoDto);

        // When
        ChatroomWithCurrentStockDto result = sut.getChatroomWithCurrentStock(chatroomId);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("chatroomId", 1L)
                .hasFieldOrPropertyWithValue("stockName", "삼성전자")
                .hasFieldOrPropertyWithValue("title", "채팅방 제목");
        then(chatroomRepository).should().findById(chatroomId);
        then(currentStockInfoService).should().getCurrentStockInfo(stockName);
    }


    @DisplayName("unchangeableId와 chatroomId가 주어지면, 채팅방을 삭제한다.")
    @Test
    void givenUnchangeableIdAndChatroomId_whenDeleting_thenDeletesChatroom() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;
        var chatroom = Chatroom.of(1L, "채팅방 제목", new HashSet<>(), LocalDateTime.now(ZoneId.of("Asia/Seoul")), "삼성전자", "test-name", unchangeableId);
        given(chatroomRepository.findById(chatroomId)).willReturn(Optional.of(chatroom));
        willDoNothing().given(chatroomRepository).deleteById(chatroomId);

        // When
        sut.deleteChatroom(unchangeableId, chatroomId);

        // Then
        then(chatroomRepository).should().findById(chatroomId);
        then(chatroomRepository).should().deleteById(chatroomId);
    }


    @DisplayName("기존에 참여한 채팅방에서 나가면, 채팅방에서 나가고 true를 반환한다.")
    @Test
    void givenUnchangeableIdAndChatroomId_whenLeavingChatroomJoinedBefore_thenLeavesChatroomAndReturnsTrue() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;
        given(githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId)).willReturn(true);
        willDoNothing().given(githubUserChatroomMappingRepository).deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId);

        // When
        Boolean result = sut.leaveChatroom(unchangeableId, chatroomId);

        // Then
        assertThat(result).isTrue();
        then(githubUserChatroomMappingRepository).should().existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);
        then(githubUserChatroomMappingRepository).should().deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId);
    }

    @DisplayName("기존에 참여하지 않은 채팅방에서 나가면, false를 반환한다.")
    @Test
    void givenUnchangeableIdAndChatroomId_whenLeavingChatroomNotJoinedBefore_thenReturnsFalse() {
        // Given
        String unchangeableId = "123456";
        Long chatroomId = 1L;
        given(githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId)).willReturn(false);

        // When
        Boolean result = sut.leaveChatroom(unchangeableId, chatroomId);

        // Then
        assertThat(result).isFalse();
        then(githubUserChatroomMappingRepository).should().existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);
        then(githubUserChatroomMappingRepository).should(never()).deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId);
    }


    @DisplayName("unchangeableId가 주어지면, 유저가 참여한 채팅창 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingChatroomList_thenReturnsChatroomList() {
        // Given
        String unchangeableId = "123456";
        List<GithubUserChatroomMapping> githubUserChatroomMappings = List.of();
        given(githubUserChatroomMappingRepository.findAllByGithubUserInfoUnchangeableId(unchangeableId)).willReturn(githubUserChatroomMappings);

        // When
        List<ChatroomDto> result = sut.getChatroomList(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(0);
        then(githubUserChatroomMappingRepository).should().findAllByGithubUserInfoUnchangeableId(unchangeableId);
        then(messageRepository).shouldHaveNoInteractions();
    }


    @DisplayName("unchangeableId가 주어지면, 유저가 참여한 채팅창을 제외한 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingChatroomList_thenReturnsUserNotJoiningChatroomList() {
        // Given
        String unchangeableId = "123456";
        List<GithubUserChatroomMapping> githubUserChatroomMappings = List.of();
        List<Chatroom> chatrooms = List.of();
        given(githubUserChatroomMappingRepository.findAllByGithubUserInfoUnchangeableId(unchangeableId)).willReturn(githubUserChatroomMappings);
        given(chatroomRepository.findAll()).willReturn(chatrooms);

        // When
        List<ChatroomDto> result = sut.getAllChatroomListExceptJoinedChatroom(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(0);
        then(githubUserChatroomMappingRepository).should().findAllByGithubUserInfoUnchangeableId(unchangeableId);
        then(chatroomRepository).should().findAll();
    }


    @DisplayName("chatroomId가 주어지면, 채팅창의 메시지 목록을 반환한다.")
    @Test
    void givenChatroomId_whenLoadingMessageList_thenReturnsMessageList() {
        // Given
        Long chatroomId = 1L;
        List<Message> messages = List.of();
        given(messageRepository.findAllByChatroomChatroomId(chatroomId)).willReturn(messages);

        // When
        List<Message> result = sut.getMessageList(chatroomId);

        // Then
        assertThat(result)
                .hasSize(0);
        then(messageRepository).should().findAllByChatroomChatroomId(chatroomId);
    }

}