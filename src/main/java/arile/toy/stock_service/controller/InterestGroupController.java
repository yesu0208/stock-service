package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.interest.InterestGroupRequest;
import arile.toy.stock_service.dto.response.GithubUserInfoResponse;
import arile.toy.stock_service.dto.response.interest.InterestGroupWithCurrentInfoResponse;
import arile.toy.stock_service.dto.response.interest.InterestStockWithCurrentInfoResponse;
import arile.toy.stock_service.dto.response.interest.SimpleInterestGroupResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.GithubUserInfoService;
import arile.toy.stock_service.service.interest.InterestGroupService;
import arile.toy.stock_service.service.StaticStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class InterestGroupController {

    private final StaticStockInfoService stockInfoService;
    private final InterestGroupService interestGroupService;
    private final GithubUserInfoService githubUserInfoService;

    @GetMapping("/interest-group")
    public String getInterestGroup(@AuthenticationPrincipal GithubUser githubUser,
                                   @RequestParam(required = false) String groupName,
                                   Model model) {

        // 비로그인 or (로그인 + groupName x) : sample(default) group, 로그인 : 해당 groupName의 interest group 조회
        InterestGroupWithCurrentInfoResponse interestGroup = (githubUser != null && groupName != null) ?
                InterestGroupWithCurrentInfoResponse.fromDto(interestGroupService.getMyGroup(githubUser.unchangeableId(), groupName)) :
                defaultInterestGroup();

        List<String> stockNames = stockInfoService.getStockNameList();

        model.addAttribute("interestGroup", interestGroup);
        model.addAttribute("stockNames", stockNames);

        return "interest-group";
    }


    @PostMapping("/interest-group")
    public String createOrUpdateInterestGroup(@AuthenticationPrincipal GithubUser githubUser,
                                              InterestGroupRequest interestGroupRequest,
                                              RedirectAttributes redirectAttrs) {

        // interestGroupRequest form data로 받아 작업을 하고, "/interest-group"으로 redirect할 때 이 정보를 전달하면 어떻까?
        // redirection하면서 열릴 페이지에 내가 만들었던 그룹 유지하고 싶다. -> 이를 위해 RedirectAttributes 사용함
        redirectAttrs.addAttribute("groupName", interestGroupRequest.groupName());

        var response = GithubUserInfoResponse.fromDto(githubUserInfoService.getGithubUserInfo((githubUser.unchangeableId())));
        Double fee = response.fee();
        interestGroupService.upsertInterestGroup(interestGroupRequest.toDto(githubUser.unchangeableId(), fee));

        return "redirect:/interest-group";
    }


    @GetMapping("/interest-group/my-groups")
    public String getMyInterestGroupList(@AuthenticationPrincipal GithubUser githubUser, Model model) {
        List<SimpleInterestGroupResponse> interestGroups = interestGroupService.getMyGroupList(githubUser.unchangeableId())
                .stream()
                .map(SimpleInterestGroupResponse::fromDto)
                .toList();

        model.addAttribute("interestGroups", interestGroups);

        return "my-groups";
    }

    @PostMapping("/interest-group/my-groups/{groupName}")
    public String deleteInterestGroup(@AuthenticationPrincipal GithubUser githubUser,
                                      @PathVariable String groupName) {
        interestGroupService.deleteInterestGroup(githubUser.unchangeableId(), groupName);
        return "redirect:/interest-group/my-groups"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    private InterestGroupWithCurrentInfoResponse defaultInterestGroup() {
        return new InterestGroupWithCurrentInfoResponse(
                null,
                null,
                List.of(
                        new InterestStockWithCurrentInfoResponse("삼성전자", null, null, null, null, 1, null, null, null, null, null, null, null))
        );
    }

}
