package arile.toy.stock_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ChatController {

    // 종목톡 메인 페이지
    @GetMapping("/stock-chats")
    public String root() {
        return "stock-chats";
    }
}
