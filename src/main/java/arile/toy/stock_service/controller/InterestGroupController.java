package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.InterestGroupRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InterestGroupController {

    // 단일 interest group 조회
    @GetMapping("/interest-group")
    public String interestGroup(InterestGroupRequest interestGroupRequest) { // redirect를 위해 parameter 후가
        return "interest-group";
    }

    // 내 interest group 생성/수정
    @PostMapping("/interest-group")
    public String createOrUpdateInterestGroup(
            InterestGroupRequest interestGroupRequest, // 폼 data로 받음
            RedirectAttributes redirectAttrs // redirection할 때, 생성/수정한 interest-group을 화면에서 유지하고 싶다
    ) {
        redirectAttrs.addFlashAttribute("interestGroupRequest", interestGroupRequest); // key-value 형식으로 data 전달

        return "redirect:/interest-group"; // redirection : PRG pattern (POST REDIRECT GET)
    }

    // 내 interest group 목록 조회
    @GetMapping("/interest-group/my-groups")
    public String myGroups() {
        return "my-groups";
    }

    // 내 interest group 삭제
    @PostMapping("/interest-group/my-groups/{groupName}")
    public String deleteMyGroup(
            @PathVariable String groupName,
            RedirectAttributes redirectAttrs // redirection
    ){
        return "redirect:/my-groups"; // redirection : PRG pattern (POST REDIRECT GET)
    }
}
