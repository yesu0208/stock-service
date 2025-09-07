package arile.toy.stock_service.controller;

import arile.toy.stock_service.domain.Chatroom;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Message;
import arile.toy.stock_service.dto.ChatMessage;
import arile.toy.stock_service.dto.response.ChatroomResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatroomResponse createChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                           @RequestParam String title) {
        return ChatroomResponse.fromDto(chatService.createChatroom(githubUser.unchangeableId(), title));
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                @PathVariable Long chatroomId,
                                @RequestParam(required = false) Long currentChatroomId) {
        return chatService.joinChatroom(githubUser.unchangeableId(), chatroomId, currentChatroomId);
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
                        message.getText()))
                .toList();
    }
}
