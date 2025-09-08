package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.StaticStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final StaticStockInfoService stockInfoService;

    // 메인 페이지 -> interest group 페이지를 보여줌
    @GetMapping("/")
    public String root() { // return : view 이름
        return "forward:/interest-group"; // interest group으로 포워딩
    }

    // 종목톡 메인 페이지
    @GetMapping("/chats")
    public String chatPage(@AuthenticationPrincipal GithubUser githubUser, Model model) {

        List<String> stockNames = stockInfoService.loadStockNameList();

        model.addAttribute("stompBrokerUrl", "https://stock-service-89300edadb9e.herokuapp.com/stomp/chats");

        model.addAttribute("currentUser", githubUser.getName());
        model.addAttribute("stockNames", stockNames);
        model.addAttribute("unchangeableId", githubUser.unchangeableId());
        return "chats";
    }
}
