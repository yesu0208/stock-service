package arile.toy.stock_service.controller;

import arile.toy.stock_service.domain.Chatroom;
import arile.toy.stock_service.domain.GithubUserInfo;
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
    public Chatroom createChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                   @RequestParam String title) {
        return chatService.createChatroom(githubUser.unchangeableId(), title);
    }

    @PostMapping("{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                @PathVariable Long chatroomId) {
        return chatService.joinChatroom(githubUser.unchangeableId(), chatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal GithubUser githubUser,
                                 @PathVariable Long chatroomId) {
        return chatService.leaveChatroom(githubUser.unchangeableId(), chatroomId);
    }

    @GetMapping
    public List<Chatroom> getChatroomList(@AuthenticationPrincipal GithubUser githubUser) {
        return chatService.getChatroomList(githubUser.unchangeableId());
    }


}
