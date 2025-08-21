package arile.toy.stock_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 메인 페이지 -> interest group 페이지를 보여줌
    @GetMapping("/")
    public String root() { // return : view 이름
        return "forward:/interest-group"; // interest group으로 포워딩
    }
}
