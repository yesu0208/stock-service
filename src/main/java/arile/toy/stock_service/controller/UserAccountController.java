package arile.toy.stock_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAccountController {

    // 내 정보 페이지
    @GetMapping("/my-account")
    public String myAccount() {
        return "my-account";
    }
}
