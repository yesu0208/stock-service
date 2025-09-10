package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.security.GithubOAuth2UserService;
import arile.toy.stock_service.service.StaticStockInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 메인 컨트룰러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired private MockMvc mvc;

    @MockBean private GithubOAuth2UserService githubOAuth2UserService; // SecurityConfig
    @MockBean private StaticStockInfoService stockInfoService;

    @DisplayName("[GET] 메인(루트) 페이지 -> 메인 뷰 (정상)")
    @Test
    void givenNothing_whenEnteringRootPage_thenShowsMainView() throws Exception {

        // Given

        // When & Then
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/interest-group"));
    }

    @DisplayName("[GET] 종목톡 메인(루트) 페이지 -> 종목톡 메인 뷰 (정상)")
    @Test
    void givenAuthenticatedUser_whenEnteringStockChatsRootPage_thenShowsStockChatsMainView() throws Exception {

        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(stockInfoService.getStockNameList()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/chats")
                        .with(oauth2Login().oauth2User(githubUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("chats"))
                .andExpect(model().attributeExists("stompBrokerUrl"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(model().attributeExists("unchangeableId"));
        then(stockInfoService).should().getStockNameList();
    }

}