package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.GithubUserInfoDto;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.dto.request.InterestGroupRequest;
import arile.toy.stock_service.dto.request.InterestStockRequest;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.GithubOAuth2UserService;
import arile.toy.stock_service.service.GithubUserInfoService;
import arile.toy.stock_service.service.InterestGroupService;
import arile.toy.stock_service.service.StockInfoService;
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
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 관심 그룹 컨트룰러 테스트")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(InterestGroupController.class)
class InterestGroupControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private FormDataEncoder formDataEncoder;

    @MockBean private GithubOAuth2UserService githubOAuth2UserService;
    @MockBean private StockInfoService stockInfoService;
    @MockBean private InterestGroupService interestGroupService;
    @MockBean private GithubUserInfoService githubUserInfoService;

    @DisplayName("[GET] 관심 그룹 페이지 -> 관심 그룹 뷰 (비로그인, 정상)")
    @Test
    void givenNothing_whenRequesting_thenShowsInterestGroupView() throws Exception {
        // Given
        given(stockInfoService.loadStockNameList()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/interest-group"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("interestGroup"))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(view().name("interest-group"));
        then(stockInfoService).should().loadStockNameList();
        then(interestGroupService).shouldHaveNoInteractions();
    }

    @DisplayName("[GET] 관심 그룹 페이지 -> 관심 그룹 뷰 (로그인, 정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowsInterestGroupView() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var groupName = "group-name";
        given(stockInfoService.loadStockNameList()).willReturn(List.of());
        given(interestGroupService.loadMyGroup(githubUser.unchangeableId(), groupName)).willReturn(
                InterestGroupWithCurrentInfoDto.of(1L, groupName, githubUser.unchangeableId(), Set.of(),
                        LocalDateTime.now(), "me", LocalDateTime.now(), "me")
        );

        // When & Then
        mvc.perform(get("/interest-group")
                        .queryParam("groupName", groupName)
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("interestGroup"))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(content().string(containsString(groupName)))
                .andExpect(view().name("interest-group")); // html 전체 검사하므로 정확하지 않은 테스트 방식
        then(stockInfoService).should().loadStockNameList();
        then(interestGroupService).should().loadMyGroup(githubUser.unchangeableId(), groupName);
    }


    @DisplayName("[POST] 관심 그룹 생성, 수정 (정상)")
    @Test
    void givenInterestGroupRequest_whenCreatingOrUpdating_thenRedirectsToInterestGroupView() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Double fee = 0.01;
        InterestGroupRequest request = InterestGroupRequest.of(
                "group-name",
                List.of(
                        InterestStockRequest.of("삼성전자", 60000, 50, 1),
                        InterestStockRequest.of("SK하이닉스", 200000, 50, 2),
                        InterestStockRequest.of("LG에너지솔루션", 350000, 50, 3)
                )
        );
        given(githubUserInfoService.loadGithubUserInfo(githubUser.unchangeableId())).willReturn(
                GithubUserInfoDto.of(githubUser.unchangeableId(), "test-id", "test-name", "test@email.com", LocalDateTime.now(), fee));
        willDoNothing().given(interestGroupService).upsertInterestGroup(request.toDto(githubUser.unchangeableId(), fee));


        // When & Then
        mvc.perform(post("/interest-group")
                .content(formDataEncoder.encode(request))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .with(oauth2Login().oauth2User(githubUser))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/interest-group?groupName={groupName}", request.groupName()));
        then(githubUserInfoService).should().loadGithubUserInfo(githubUser.unchangeableId());
        then(interestGroupService).should().upsertInterestGroup(request.toDto(githubUser.unchangeableId(), fee));
    }


    @DisplayName("[GET] 내 관심 그룹 목록 페이지 -> 내 관심 그룹 목록 뷰 (로그인, 정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowMyInterestGroup() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(interestGroupService.loadMyGroups(githubUser.unchangeableId())).willReturn(List.of());

        // When & Then
        mvc.perform(get("/interest-group/my-groups")
                .with(oauth2Login().oauth2User(githubUser))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("interestGroups", List.of()))
                .andExpect(view().name("my-groups"));
        then(interestGroupService).should().loadMyGroups(githubUser.unchangeableId());
    }


    @DisplayName("[GET] 내 관심 그룹 목록 페이지 -> redirection (비로그인, 정상)")
    @Test
    void givenNothing_whenRequesting_thenRedirectToLogin() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/interest-group/my-groups"))
                .andExpect(status().is3xxRedirection())
                // redirectedUrl, redirectedUrlTemplate, redirectedUrlPattern
                .andExpect(redirectedUrlPattern("**/oauth2/authorization/github"));
        then(interestGroupService).shouldHaveNoInteractions();
    }


    @DisplayName("[POST] 내 관심 그룹 삭제 (정상)")
    @Test
    void givenAuthenticatedUserAndGroupName_whenDeleting_thenRedirectsToMyInterestGroup() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        String groupName = "group_name";
        willDoNothing().given(interestGroupService).deleteInterestGroup(githubUser.unchangeableId(), groupName);

        // When & Then
        mvc.perform(post("/interest-group/my-groups/{groupName}", groupName)
                // 브라우저에서 form 요청 시 → Spring Security가 자동으로 hidden input(_csrf)을 심어주고, 제출 시 함께 전달됨.
                // MockMvc는 실제 브라우저가 아니기 때문에 자동으로 CSRF 토큰을 심어주지 않음.
                .with(csrf())
                .with(oauth2Login().oauth2User(githubUser))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/interest-group/my-groups"));
        then(interestGroupService).should().deleteInterestGroup(githubUser.unchangeableId(), groupName);
    }


}