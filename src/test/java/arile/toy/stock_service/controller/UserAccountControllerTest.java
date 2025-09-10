package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.GithubUserCurrentAccountDto;
import arile.toy.stock_service.dto.GithubUserInfoDto;
import arile.toy.stock_service.dto.request.GithubUserInfoRequest;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.security.GithubOAuth2UserService;
import arile.toy.stock_service.service.GithubUserInfoService;
import arile.toy.stock_service.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 회원 정보 컨트룰러 테스트")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest {

    @Autowired MockMvc mvc;
    @Autowired private FormDataEncoder formDataEncoder;

    @MockBean private GithubUserInfoService githubUserInfoService;
    @MockBean private GithubOAuth2UserService githubOAuth2UserService;

    @DisplayName("[GET] 내 정보 페이지 -> 내 정보 뷰 (정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowMyAccountView() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(githubUserInfoService.getGithubUserInfo(githubUser.unchangeableId()))
                .willReturn(GithubUserInfoDto.of(
                        githubUser.unchangeableId(), githubUser.id(), githubUser.name(), githubUser.email(), LocalDateTime.now(), 0.01));
        given(githubUserInfoService.getGithubUserCurrentAccount(githubUser.unchangeableId()))
                .willReturn(GithubUserCurrentAccountDto.of(
                        githubUser.unchangeableId(), 1000, 1000, 1000, 1000, "10.0%"));


        // When & Then
        mvc.perform(get("/my-account")
                .with(oauth2Login().oauth2User(githubUser))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("nickname", githubUser.name()))
                .andExpect(model().attribute("unchangeableId", githubUser.unchangeableId()))
                .andExpect(model().attribute("email", githubUser.email()))
                .andExpect(model().attributeExists("lastLoginAt"))
                .andExpect(model().attributeExists("fee"))
                .andExpect(model().attributeExists("totalBuyingPriceStr"))
                .andExpect(model().attributeExists("totalValuationStr"))
                .andExpect(model().attributeExists("totalUnrealizedPLStr"))
                .andExpect(model().attributeExists("totalRealizedPLStr"))
                .andExpect(model().attributeExists("rateOfReturn"))
                .andExpect(model().attributeExists("totalRealizedPL"))
                .andExpect(view().name("my-account"));
        then(githubUserInfoService).should().getGithubUserInfo(githubUser.unchangeableId());
        then(githubUserInfoService).should().getGithubUserCurrentAccount(githubUser.unchangeableId());

    }


    @DisplayName("[Post] 내 수수료 정보 수정하기 (정상)")
    @Test
    void givenAuthenticatedUser_whenUpdating_thenUpdateMyFee() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var githubUserInfoRequest = new GithubUserInfoRequest(10.0);
        willDoNothing().given(githubUserInfoService).updateGithubUserFee(githubUser.unchangeableId(), githubUserInfoRequest.feeRate());

        // When & Then
        mvc.perform(post("/my-account")
                // 웹사이트 → 웹서버 : Form Data → Request 객체
                // 테스트에서는 브라우저가 없으므로, Java 객체 → Form Data로 변환 후 MockMvc에 전달
                .content(formDataEncoder.encode(githubUserInfoRequest))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // APPLICATION_JSON type이 아닌 form 요청
                .with(csrf()) // 이 POST 요청은 자동으로 csrf정보가 포함해서 들어가게 될 것.
                .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/my-account"));
        then(githubUserInfoService).should().updateGithubUserFee(githubUser.unchangeableId(), githubUserInfoRequest.feeRate());
    }

}