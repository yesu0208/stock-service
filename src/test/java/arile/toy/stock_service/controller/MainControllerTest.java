//package arile.toy.stock_service.controller;
//
//import arile.toy.stock_service.config.SecurityConfig;
//import arile.toy.stock_service.service.GithubOAuth2UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@DisplayName("[Controller] 메인(루트) 컨트룰러 테스트")
//@Import(SecurityConfig.class)
//@WebMvcTest(MainController.class)
//class MainControllerTest {
//
//    @Autowired private MockMvc mvc;
//
//    @MockBean private GithubOAuth2UserService githubOAuth2UserService; // SecurityConfig
//
//    @DisplayName("[GET] 메인(루트) 페이지 -> 메인 뷰 (정상)")
//    @Test
//    void givenNothing_whenEnteringRootPage_thenShowsMainView() throws Exception {
//
//        // Given
//
//        // When & Then
//        mvc.perform(get("/"))
//                .andExpect(status().isOk())
////                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(forwardedUrl("/interest-group"));
//
//    }
//
//}