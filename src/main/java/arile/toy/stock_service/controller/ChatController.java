package arile.toy.stock_service.controller;

import arile.toy.stock_service.domain.Chatroom;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Message;
import arile.toy.stock_service.dto.ChatMessage;
import arile.toy.stock_service.dto.ChatroomWithCurrentStockDto;
import arile.toy.stock_service.dto.request.ChatroomRequest;
import arile.toy.stock_service.dto.response.ChatroomResponse;
import arile.toy.stock_service.dto.response.ChatroomWithCurrentStockResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/chats")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ChatroomResponse createChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                           @RequestBody ChatroomRequest chatroomRequest) {
        var chatroomResponse = ChatroomResponse.fromDto(chatService.createChatroom(
                githubUser.unchangeableId(), chatroomRequest.title(), chatroomRequest.stockName(), githubUser.name()));

        // STOMP로 새 채팅방 알림
        simpMessagingTemplate.convertAndSend("/sub/chats/room-created", chatroomResponse);

        return chatroomResponse;
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                @PathVariable Long chatroomId,
                                @RequestParam(required = false) Long currentChatroomId) {
        return chatService.joinChatroom(githubUser.unchangeableId(), chatroomId, currentChatroomId);
    }


    @GetMapping("/{chatroomId}")
    public ChatroomWithCurrentStockResponse getChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                                        @PathVariable Long chatroomId) {
        return ChatroomWithCurrentStockResponse.fromDto(chatService.getChatroomWithCurrentStock(chatroomId));
    }


    @DeleteMapping("/delete/{chatroomId}")
    public void deleteChatroom(@AuthenticationPrincipal GithubUser githubUser,
                               @PathVariable Long chatroomId) {

        chatService.deleteChatroom(githubUser.unchangeableId(), chatroomId);

        // STOMP로 모든 구독자에게 삭제 알림 전송
        simpMessagingTemplate.convertAndSend("/sub/chats/room-deleted", chatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                 @PathVariable Long chatroomId) {
        return chatService.leaveChatroom(githubUser.unchangeableId(), chatroomId);
    }

    @GetMapping
    public List<ChatroomResponse> getChatroomList(@AuthenticationPrincipal GithubUser githubUser) {
        return chatService.getChatroomList(githubUser.unchangeableId())
                .stream()
                .map(ChatroomResponse::fromDto)
                .toList();
    }

    @GetMapping("/total")
    public List<ChatroomResponse> getAllChatroomListExceptJoined(@AuthenticationPrincipal GithubUser githubUser) {
        return chatService.getAllChatroomListExceptJoined(githubUser.unchangeableId())
                .stream()
                .map(ChatroomResponse::fromDto)
                .toList();
    }


    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessagelist(@PathVariable Long chatroomId) {
        return chatService.getMessageList(chatroomId).stream()
                .map(message -> new ChatMessage(message.getGithubUserInfo().getName(),
                        message.getText(),message.getCreatedAt(), message.getMessageType(), message.getGithubUserInfo().getUnchangeableId()))
                .toList();
    }
}
