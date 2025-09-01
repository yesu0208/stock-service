package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.CurrentStockInfoDto;
import arile.toy.stock_service.service.CurrentStockInfoService;
import arile.toy.stock_service.service.GithubOAuth2UserService;
import arile.toy.stock_service.service.StaticStockInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 현재 주가정보 컨트룰러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest(CurrentStockInfoController.class)
class CurrentStockInfoControllerTest {

    // 실제 로직
    @Autowired private MockMvc mvc;

    // 가짜 로직 : willReturn, ...
    @MockBean private GithubOAuth2UserService githubOAuth2UserService; // why?
    @MockBean private CurrentStockInfoService currentStockInfoService;
    @MockBean private StaticStockInfoService stockInfoService;

    @DisplayName("[GET] 종목명 목록 페이지 -> 종목명 목록 뷰 (정상)")
    @Test
    void givenNothing_whenRequesting_thenShowsStocksView() throws Exception {
        // Given
        given(stockInfoService.loadStockNameList()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(view().name("stocks"));
        then(stockInfoService).should().loadStockNameList();
    }

    @DisplayName("[GET] 종목명 -> 종목정보 (정상)")
    @Test
    void givenStockName_whenRequesting_thenReturnsStockInformation() throws Exception {
        // Given
        var stockName = "삼성전자";
        given(currentStockInfoService.getCurrentStockInfo(stockName)).willReturn(
                CurrentStockInfoDto.of("123456", "삼성전자", "OPEN", "KOSPI",
                        70000, 500, 0.1, "2", 70100, 60900,
                        90000, 50000, 5, 69500,
                        123546L, 123456L, LocalDateTime.now())
        );

        // When & Then
        mvc.perform(get("/stocks/info")
                .queryParam("stockName", stockName) // @RequestParam
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stockName").value("삼성전자"));
        then(currentStockInfoService).should().getCurrentStockInfo(stockName);
    }

}