package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.GithubUserInfoRequest;
import arile.toy.stock_service.dto.response.GithubUserCurrentAccountResponse;
import arile.toy.stock_service.dto.response.GithubUserInfoResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.GithubUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;

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

        GithubUserCurrentAccountResponse current_response =
                GithubUserCurrentAccountResponse.fromDto(githubUserInfoService.loadGithubUserCurrentAccount(githubUser.unchangeableId()));

        model.addAttribute("nickname", githubUser.name());
        model.addAttribute("unchangeableId", githubUser.unchangeableId());
        model.addAttribute("email", githubUser.email());
        model.addAttribute("lastLoginAt", response.lastLoginAt());
        model.addAttribute("fee", response.fee());

        DecimalFormat df = new DecimalFormat("#,###"); // 천 단위 콤마

        model.addAttribute("totalBuyingPriceStr", df.format(current_response.totalBuyingPrice()));
        model.addAttribute("totalValuationStr", df.format(current_response.totalValuation()));
        model.addAttribute("totalUnrealizedPLStr", df.format(current_response.totalUnrealizedPL()));
        model.addAttribute("totalRealizedPLStr", df.format(current_response.totalRealizedPL()));
        model.addAttribute("rateOfReturn", current_response.rateOfReturnString());

        // 원본 숫자도 넘겨서 색상 조건에는 그대로 사용
        model.addAttribute("totalRealizedPL", current_response.totalRealizedPL());

        return "my-account";
    }

    // 내 fee 수정
    @PostMapping("/my-account")
    public String updateMyFee(
            @AuthenticationPrincipal GithubUser githubUser,
            GithubUserInfoRequest githubUserInfoRequest // 폼 data로 받음
    ) {
        githubUserInfoService.updateGithubUserFee(githubUser.unchangeableId(), githubUserInfoRequest.fee());

        return "redirect:/my-account"; // redirection : PRG pattern (POST REDIRECT GET)
    }
}
