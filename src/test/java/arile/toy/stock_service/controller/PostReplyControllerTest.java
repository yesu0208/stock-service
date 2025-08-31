package arile.toy.stock_service.controller;

import arile.toy.stock_service.config.SecurityConfig;
import arile.toy.stock_service.dto.PostDto;
import arile.toy.stock_service.dto.request.PostRequest;
import arile.toy.stock_service.dto.request.ReplyRequest;
import arile.toy.stock_service.dto.response.PostResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.GithubOAuth2UserService;
import arile.toy.stock_service.service.PostService;
import arile.toy.stock_service.service.ReplyService;
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

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 게시물/댓글 컨트룰러 테스트")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(PostReplyController.class)
class PostReplyControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private FormDataEncoder formDataEncoder;

    @MockBean private GithubOAuth2UserService githubOAuth2UserService;
    @MockBean private PostService postService;
    @MockBean private ReplyService replyService;
    @MockBean private StockInfoService stockInfoService;

    @DisplayName("[GET] 게시물/댓글 페이지 -> 게시물/댓글 뷰 (로그인, 처음 생성, 정상)")
    @Test
    void givenNothing_whenRequesting_thenShowsEmptyPost() throws Exception {

        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(stockInfoService.loadStockNameList()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/post")
                .with(oauth2Login().oauth2User(githubUser))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(model().attributeExists("post"))
                // <section th:if="${post.postId != null}" ...>
                // <div th:each="reply : ${replies}"> -> th:if 조건 때문에 댓글 섹션 전체가 렌더링되지 않음.
//                .andExpect(model().attributeExists("replies"))
                .andExpect(model().attribute("currentUserId", githubUser.unchangeableId()))
                .andExpect(view().name("post"));
        then(stockInfoService).should().loadStockNameList();
        then(postService).shouldHaveNoInteractions();
        then(replyService).shouldHaveNoInteractions();
    }


    @DisplayName("[GET] 게시물/댓글 페이지 -> 게시물/댓글 뷰 (로그인, 내 게시물 조회, 정상)")
    @Test
    void givenAuthenticatedUserAndPostId_whenRequesting_thenShowsPostAndReplies() throws Exception {

        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        given(stockInfoService.loadStockNameList()).willReturn(List.of());
        given(postService.loadPost(githubUser.unchangeableId(), postId)).willReturn(
                PostResponse.fromDto(PostDto.of(1L, "post", "삼성전자", "삼성전자", 0L,
                        1L, 1L, ZonedDateTime.now(), ZonedDateTime.now(), "test-name", "12345"),
                        true, false)
        );
        given(replyService.loadAllRepliesByPostId(postId)).willReturn(List.of());

        // When & Then
        mvc.perform(get("/post")
                        // String, String
                        .queryParam("postId", String.valueOf(postId))
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("stockNames"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("replies", List.of()))
                .andExpect(model().attribute("currentUserId", githubUser.unchangeableId()))
                .andExpect(view().name("post"));
        then(stockInfoService).should().loadStockNameList();
        then(postService).should().loadPost(githubUser.unchangeableId(), postId);
        then(replyService).should().loadAllRepliesByPostId(postId);
    }



    @DisplayName("[GET] 게시물 목록 페이지 -> 게시물 목록 뷰 (로그인, 전체 게시물 목록, 정상)")
    @Test
    void givenNothing_whenRequesting_thenShowsAllPostList() throws Exception {

        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(postService.loadAllSimplePosts()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/posts")
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("posts", List.of()))
                .andExpect(view().name("posts"));
        then(postService).should().loadAllSimplePosts();
    }



    @DisplayName("[GET] 게시물 목록 페이지 -> 게시물 목록 뷰 (로그인, 내 게시물 목록, 정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowsAllMyPostList() throws Exception {

        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        given(postService.loadAllMySimplePosts(githubUser.unchangeableId())).willReturn(List.of());

        // When & Then
        mvc.perform(get("/my-posts")
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("posts", List.of()))
                .andExpect(view().name("my-posts"));
        then(postService).should().loadAllMySimplePosts(githubUser.unchangeableId());
    }


    @DisplayName("[POST] 게시물 생성 : postId는 null (로그인, 정상)")
    @Test
    void givenPostRequest_whenCreating_thenRedirectsToPostView() throws Exception {

        // Given
        Long postId = null;
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var postRequest = PostRequest.of("post", "삼성전자", "삼성전자내용", "test-name", "12345");
        var upsertPostId = 1L;
        given(postService.upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId)).willReturn(upsertPostId);

        // When & Then
        mvc.perform(post("/post")
                        .content(formDataEncoder.encode(postRequest))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/post?postId={upsertPostId}", upsertPostId));
        then(postService).should().upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId);
    }


    @DisplayName("[POST] 게시물 수정 : postId는 주어짐 (로그인, 정상)")
    @Test
    void givenPostRequest_whenUpdating_thenRedirectsToPostView() throws Exception {

        // Given
        Long postId = 1L;
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        var postRequest = PostRequest.of("post", "삼성전자", "삼성전자내용", "test-name", "12345");
        var upsertPostId = 1L;
        given(postService.upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId)).willReturn(upsertPostId);

        // When & Then
        mvc.perform(post("/post")
                        .queryParam("postId", String.valueOf(postId))
                        .content(formDataEncoder.encode(postRequest))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/post?postId={upsertPostId}", upsertPostId));
        then(postService).should().upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId);
    }



    @DisplayName("[POST] 내 게시물 삭제 (정상)")
    @Test
    void givenAuthenticatedUserAndPostId_whenDeleting_thenRedirectsToMyPosts() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        willDoNothing().given(postService).deletePost(githubUser.unchangeableId(), postId);

        // When & Then
        mvc.perform(post("/my-posts/{postId}", postId)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-posts"));
        then(postService).should().deletePost(githubUser.unchangeableId(), postId);
    }


    @DisplayName("[POST] 게시물 좋아요 누르기 (정상)")
    @Test
    void givenAuthenticatedUserAndPostId_whenToggleLikes_thenPostLikesChanges() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        given(postService.toggleLike(postId, githubUser.unchangeableId())).willReturn(
                PostResponse.fromDto(PostDto.of(1L, "post", "삼성전자", "삼성전자", 0L,
                                1L, 1L, ZonedDateTime.now(), ZonedDateTime.now(), "test-name", "12345"),
                        true, false)
        );

        // When & Then
        mvc.perform(post("/post/likes/{postId}", postId)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isLiking").value(true))
                .andExpect(jsonPath("$.likesCount").value(1L))
                .andExpect(jsonPath("$.isDisliking").value(false))
                .andExpect(jsonPath("$.dislikesCount").value(1L));
        then(postService).should().toggleLike(postId, githubUser.unchangeableId());
    }


    @DisplayName("[POST] 게시물 싫어요 누르기 (정상)")
    @Test
    void givenAuthenticatedUserAndPostId_whenToggleDislikes_thenPostDislikesChanges() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        given(postService.toggleDislike(postId, githubUser.unchangeableId())).willReturn(
                PostResponse.fromDto(PostDto.of(1L, "post", "삼성전자", "삼성전자", 0L,
                                1L, 1L, ZonedDateTime.now(), ZonedDateTime.now(), "test-name", "12345"),
                        true, false)
        );

        // When & Then
        mvc.perform(post("/post/dislikes/{postId}", postId)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isLiking").value(true))
                .andExpect(jsonPath("$.likesCount").value(1L))
                .andExpect(jsonPath("$.isDisliking").value(false))
                .andExpect(jsonPath("$.dislikesCount").value(1L));
        then(postService).should().toggleDislike(postId, githubUser.unchangeableId());
    }


    @DisplayName("[POST] 댓글 생성, 수정 (정상)")
    @Test
    void givenReplyRequest_whenCreatingOrUpdating_thenRedirectsToPostView() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        Long replyId = 1L;
        ReplyRequest request = ReplyRequest.of("reply-body", "test-name", githubUser.unchangeableId());
        willDoNothing().given(replyService).upsertReply(request.toDto(githubUser.name(), githubUser.unchangeableId()), postId, replyId);

        // When & Then
        mvc.perform(post("/reply/upsert/{postId}", postId)
                        .queryParam("replyId", String.valueOf(replyId))
                        .content(formDataEncoder.encode(request))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/post?postId={postId}", postId));
        then(replyService).should().upsertReply(request.toDto(githubUser.name(), githubUser.unchangeableId()), postId, replyId);
    }



    @DisplayName("[POST] 내 댓글 삭제 (정상)")
    @Test
    void givenAuthenticatedUserAndReplyId_whenDeleting_thenRedirectsToPost() throws Exception {
        // Given
        var githubUser = new GithubUser("12345", "test-id", "test-name", "test@eamil.com");
        Long postId = 1L;
        Long replyId = 1L;
        willDoNothing().given(replyService).deleteReply(githubUser.unchangeableId(), postId, replyId);

        // When & Then
        mvc.perform(post("/reply/delete/{postId}", postId)
                        .queryParam("replyId", String.valueOf(replyId))
                        .with(csrf())
                        .with(oauth2Login().oauth2User(githubUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/post?postId={postId}", postId));
        then(replyService).should().deleteReply(githubUser.unchangeableId(), postId, replyId);
    }
}