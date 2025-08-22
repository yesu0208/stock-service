package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.security.GithubUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAccountController {

    // 내 정보 페이지
    @GetMapping("/my-account")
    public String myAccount(
            @AuthenticationPrincipal GithubUser githubUser, // 인증정보임을 알려주는 @AuthenticationPrincipal
            Model model) {
        model.addAttribute("nickname", githubUser.name());
        model.addAttribute("email", githubUser.email());
        return "my-account";
    }
}
