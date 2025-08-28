package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.GithubUserInfoRequest;
import arile.toy.stock_service.dto.request.InterestGroupRequest;
import arile.toy.stock_service.dto.response.GithubUserInfoResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.GithubUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class UserAccountController {

    private final GithubUserInfoService githubUserInfoService;

    // 내 정보 페이지
    @GetMapping("/my-account")
    public String myAccount(
            @AuthenticationPrincipal GithubUser githubUser, // 인증정보임을 알려주는 @AuthenticationPrincipal
            Model model) {

        GithubUserInfoResponse response =
                GithubUserInfoResponse.fromDto(githubUserInfoService.loadGithubUserInfo(githubUser.unchangeableId()));

        model.addAttribute("nickname", githubUser.name());
        model.addAttribute("unchangeableId", githubUser.unchangeableId());
        model.addAttribute("email", githubUser.email());
        model.addAttribute("lastLoginAt", response.lastLoginAt());
        model.addAttribute("fee", response.fee());
        return "my-account";
    }
}
