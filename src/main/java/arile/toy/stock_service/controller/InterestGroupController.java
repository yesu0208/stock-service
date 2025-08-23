package arile.toy.stock_service.controller;

import arile.toy.stock_service.domain.StockInfo;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.dto.request.InterestGroupRequest;
import arile.toy.stock_service.dto.response.*;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.InterestGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import arile.toy.stock_service.repository.StockInfoRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class InterestGroupController {

    private final StockInfoRepository stockInfoRepository;
    private final InterestGroupService interestGroupService;

    // 단일 interest group 조회
    @GetMapping("/interest-group")
    public String interestGroup(
            @AuthenticationPrincipal GithubUser githubUser,
            @RequestParam(required = false) String groupName, // 필수값이 아닌 옵션값 (내 관심그룹 목록 페이지에서 관심그룹 이름 클릭 시에만 적용)
            Model model) { // redirect를 위해 parameter 후가

        // 비로그인 or 로그인 + groupName x : sample group, 로그인 : 해당 groupName의 interest group 조회
        InterestGroupWithCurrentInfoResponse interestGroup = (githubUser != null && groupName != null) ?
                InterestGroupWithCurrentInfoResponse.fromDto(interestGroupService.loadMyGroup(githubUser.id(), groupName)) :
                defaultInterestGroup(groupName);

        List<StockInfo> stockInfo = stockInfoRepository.findAll();
        List<String> stockNames = stockInfo.stream()
                .map(StockInfo::getStockName)
                .toList();

        model.addAttribute("interestGroup", interestGroup);
        model.addAttribute("stockNames", stockNames);

        return "interest-group";
    }

    // 내 interest group 생성/수정
    @PostMapping("/interest-group")
    public String createOrUpdateInterestGroup(
            @AuthenticationPrincipal GithubUser githubUser,
            InterestGroupRequest interestGroupRequest, // 폼 data로 받음
            RedirectAttributes redirectAttrs // redirection할 때, 생성/수정한 interest-group을 화면에서 유지하고 싶다
    ) {
        // redirectAttrs.addFlashAttribute("interestGroupRequest", interestGroupRequest); // key-value 형식으로 data 전달

        // interestGroupRequest form data로 받아 작업을 하고, "/interest-group"으로 redirect할 때 이 정보를 전달하면 어떻까?
        // redirection하면서 열릴 페이지에 내가 만들었던 것 유지하고 싶다. -> 이를 위해 RedirectAttributes가 필요
        redirectAttrs.addAttribute("groupName", interestGroupRequest.groupName());

        interestGroupService.upsertInterestGroup(interestGroupRequest.toDto(githubUser.id()));

        return "redirect:/interest-group"; // redirection : PRG pattern (POST REDIRECT GET)
    }

    // 내 interest group 목록 조회
    @GetMapping("/interest-group/my-groups")
    public String myGroups(@AuthenticationPrincipal GithubUser githubUser,
                           Model model) {
        List<SimpleInterestGroupResponse> interestGroups = interestGroupService.loadMyGroups(githubUser.id())
                .stream()
                .map(SimpleInterestGroupResponse::fromDto)
                .toList();

        model.addAttribute("interestGroups", interestGroups);

        return "my-groups";
    }

    // 내 interest group 삭제
    @PostMapping("/interest-group/my-groups/{groupName}")
    public String deleteMyGroup(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable String groupName
    ){
        interestGroupService.deleteInterestGroup(githubUser.id(), groupName);
        return "redirect:/my-groups"; // redirection : PRG pattern (POST REDIRECT GET)
    }





    // 기본 interest group
    private InterestGroupWithCurrentInfoResponse defaultInterestGroup(String groupName) {
        return new InterestGroupWithCurrentInfoResponse(
                groupName != null ? groupName : "group_name", // groupName 받았으면 그대로 쓰고, 안받았으면 기본값
                "Arile",
                List.of(
                        new InterestStockWithCurrentInfoResponse("삼성전자보통주", null, null, null, null, 1, null, null, null)
                )
        );
    }

    // sample group 목록
    private static List<SimpleInterestGroupResponse> mySampleGroups() {
        return List.of(
                new SimpleInterestGroupResponse("group_name1", "Arile", LocalDate.of(2025, 1, 1).atStartOfDay()),
                new SimpleInterestGroupResponse("group_name2", "Arile", LocalDate.of(2025, 2, 2).atStartOfDay()),
                new SimpleInterestGroupResponse("group_name3", "Arile", LocalDate.of(2025, 3, 3).atStartOfDay())
        );
    }

}
