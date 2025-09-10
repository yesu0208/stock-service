package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.chatdto.ChatroomDto;
import arile.toy.stock_service.dto.chatdto.ChatroomWithCurrentStockDto;
import arile.toy.stock_service.dto.request.chat.ChatroomRequest;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.chat.ChatService;
import arile.toy.stock_service.service.security.GithubOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 종목톡(채팅) 컨트룰러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private GithubOAuth2UserService githubOAuth2UserService;
    @MockBean private ChatService chatService;
    @MockBean private SimpMessagingTemplate simpMessagingTemplate;


    @DisplayName("[POST] 채팅방 생성 (정상)")
    @Test
    void givenAuthenticatedUserAndChatroomRequest_whenCreating_thenReturnsChatroomResponse() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var chatroomRequest = ChatroomRequest.of("채팅방 제목", "삼성전자", "test-name");
        var chatroomDto = ChatroomDto.of(1L, "채팅방 제목", 2, LocalDateTime.now(), "삼성전자",
                "test-name", "123456", false);
        String jsonBody = objectMapper.writeValueAsString(chatroomRequest);
        given(chatService.createChatroom(githubUser.unchangeableId(), chatroomRequest.title(),
                chatroomRequest.stockName(), githubUser.name())).willReturn(chatroomDto);

        // When & Then
        mvc.perform(post("/api/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("채팅방 제목"));
        then(chatService).should().createChatroom(githubUser.unchangeableId(), chatroomRequest.title(),
                chatroomRequest.stockName(), githubUser.name());
    }


    @DisplayName("[GET] 현재 종목정보를 담는 채팅방 반환 (정상)")
    @Test
    void givenAuthenticatedUserAndChatroomId_whenRequesting_thenReturnsChatroomWithCurrentStockResponse() throws Exception {
        // Given
        Long chatroomId = 1L;
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var chatroomWithCurrentStockDto = ChatroomWithCurrentStockDto.of(1L, "채팅방 제목", 1,
                LocalDateTime.now(), "삼성전자", "test-name", false, "OPEN",
                50000, 500, 1.0);
        given(chatService.getChatroomWithCurrentStock(chatroomId)).willReturn(chatroomWithCurrentStockDto);

        // When & Then
        mvc.perform(get("/api/chats/{chatroomId}", chatroomId)
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chatroomId").value(1L));
        then(chatService).should().getChatroomWithCurrentStock(chatroomId);
    }


    @DisplayName("[DELETE] 내 채팅방 삭제 (정상)")
    @Test
    void givenAuthenticatedUserAndChatroomId_whenDeleting_thenDeletesChatroom() throws Exception {
        // Given
        var githubUser = new GithubUser("123456", "test-id", "test-name", "test@eamil.com");
        Long chatroomId = 1L;
        willDoNothing().given(chatService).deleteChatroom(githubUser.unchangeableId(), chatroomId);

        // When & Then
        mvc.perform(delete("/api/chats/delete/{chatroomId}", chatroomId)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk());
        then(chatService).should().deleteChatroom(githubUser.unchangeableId(), chatroomId);
    }


    @DisplayName("[DELETE] 채팅방 나가기 (정상)")
    @Test
    void givenAuthenticatedUserAndChatroomId_whenLeaving_thenLeavesChatroom() throws Exception {
        // Given
        var githubUser = new GithubUser("123456", "test-id", "test-name", "test@eamil.com");
        Long chatroomId = 1L;
        given(chatService.leaveChatroom(githubUser.unchangeableId(), chatroomId)).willReturn(true);

        // When & Then
        mvc.perform(delete("/api/chats/{chatroomId}", chatroomId)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
        then(chatService).should().leaveChatroom(githubUser.unchangeableId(), chatroomId);
    }

    @DisplayName("[GET] 내가 참여한 채팅방 목록 반환 (정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenReturnsChatroomList() throws Exception {
        // Given
        var githubUser = new GithubUser("123456", "test-id", "test-name", "test@eamil.com");

        // When & Then
        mvc.perform(get("/api/chats")
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk());
        then(chatService).should().getChatroomList(githubUser.unchangeableId());
    }

    @DisplayName("[GET] 채팅방의 메시지 목록 반환 (정상)")
    @Test
    void givenAuthenticatedUserAndChatroomId_whenRequesting_thenReturnsMessageList() throws Exception {
        // Given
        var githubUser = new GithubUser("123456", "test-id", "test-name", "test@eamil.com");

        // When & Then
        mvc.perform(get("/api/chats")
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk());
        then(chatService).should().getChatroomList(githubUser.unchangeableId());
    }

}