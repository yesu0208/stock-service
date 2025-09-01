package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.response.CurrentStockInfoResponse;
import arile.toy.stock_service.service.CurrentStockInfoService;
import arile.toy.stock_service.service.StaticStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CurrentStockInfoController {

    private final CurrentStockInfoService currentStockInfoService;
    private final StaticStockInfoService stockInfoService;

    @GetMapping("/stocks")
    public String stockPage(Model model) {
        List<String> stockNames = stockInfoService.loadStockNameList();
        model.addAttribute("stockNames", stockNames);
        return "stocks"; // stocks.html 렌더링
    }

    @ResponseBody
    @GetMapping("/stocks/info")
    public CurrentStockInfoResponse getStockInfo(@RequestParam String stockName) {

        return CurrentStockInfoResponse.fromDto(
                currentStockInfoService.getCurrentStockInfo(stockName)
        );

    }
}

