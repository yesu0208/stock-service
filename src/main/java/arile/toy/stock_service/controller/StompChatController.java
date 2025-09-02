package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.ChatMessage;
import arile.toy.stock_service.dto.security.GithubUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class StompChatController {

    @MessageMapping("/chats")
    @SendTo("/sub/chats")
    public ChatMessage handleMessage(@AuthenticationPrincipal GithubUser githubUser,
            @Payload Map<String, String> payload) { // payload 자체는 JSON String -> String 내에서 message 속성값 빼내기

        return new ChatMessage(githubUser.getName(), payload.get("message"));
    }
}

