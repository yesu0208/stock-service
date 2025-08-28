package arile.toy.stock_service.controller;

import arile.toy.stock_service.domain.StockInfo;
import arile.toy.stock_service.dto.request.InterestGroupRequest;
import arile.toy.stock_service.dto.request.PostRequest;
import arile.toy.stock_service.dto.response.InterestGroupWithCurrentInfoResponse;
import arile.toy.stock_service.dto.response.InterestStockWithCurrentInfoResponse;
import arile.toy.stock_service.dto.response.PostResponse;
import arile.toy.stock_service.dto.response.SimplePostResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.repository.StockInfoRepository;
import arile.toy.stock_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final StockInfoRepository stockInfoRepository;
    private final PostService postService;

    // 단일 post 조회
    @GetMapping("/post")
    public String post(@AuthenticationPrincipal GithubUser githubUser,
                        @RequestParam(required = false) Long postId, // postId 들어가면 해당 내용, 아니면 빈 내용 보여주기
                       Model model) {

        List<StockInfo> stockInfo = stockInfoRepository.findAll();
        List<String> stockNames = stockInfo.stream()
                .map(StockInfo::getStockName)
                .toList();

        // postId x : sample post, postId : 해당 postId의 post 조회
        PostResponse post = (postId != null) ?
                postService.loadPost(githubUser.unchangeableId(), postId) : defaultPost(githubUser);

        model.addAttribute("stockNames", stockNames);
        model.addAttribute("post", post); // 여기에 게시물의 unchangeableId가 포함되어 있음.
        // 현재 사용자인 경우에만 게시물 수정하는 섹션 활성화하기 위해 현재 유저의 unchangeableId와 게시물의 unchangeableId를 비교하기 위해 추가
        model.addAttribute("currentUserId", githubUser.unchangeableId());

        return "post";
    }

    // 모든 post 목록 조회 (내용 제외)
    @GetMapping("/posts")
    public String allSimplePosts(Model model) {
        List<SimplePostResponse> posts = postService.loadAllSimplePosts()
                        .stream()
                        .map(SimplePostResponse::fromDto)
                        .toList();
        model.addAttribute("posts", posts);
        return "posts";
    }

    // 내 post 목록 조회 (내용 제외)
    @GetMapping("/my-posts")
    public String allMySimplePosts(@AuthenticationPrincipal GithubUser githubUser,
                                    Model model) {
        List<SimplePostResponse> posts = postService.loadAllMySimplePosts(githubUser)
                .stream()
                .map(SimplePostResponse::fromDto)
                .toList();
        model.addAttribute("posts", posts);
        return "my-posts";
    }

    // post 생성/수정
    @PostMapping("/post")
    public String createOrUpdateMyPost(
            @AuthenticationPrincipal GithubUser githubUser,
            PostRequest postRequest, // 폼 data로 받음
            RedirectAttributes redirectAttrs // redirection할 때, 생성/수정한 interest-group을 화면에서 유지하고 싶다
    ) {

        Long upsertPostId = postService.upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()));

        redirectAttrs.addAttribute("postId", upsertPostId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    // post 삭제
    @PostMapping("/my-posts/{postId}")
    public String deleteMyPost(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable Long postId
    ){
        postService.deletePost(githubUser.unchangeableId(), postId);
        return "redirect:/my-posts"; // redirection : PRG pattern (POST REDIRECT GET)
    }

    @PostMapping("/post/likes/{postId}")
    @ResponseBody
    public Map<String, Object> toggleLike(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable Long postId
    ) {
        var postResponse = postService.toggleLike(postId, githubUser.unchangeableId());
        Map<String, Object> result = new HashMap<>();
        result.put("isLiking", postResponse.isLiking());
        result.put("likesCount", postResponse.likesCount());
        result.put("isDisliking", postResponse.isDisliking());
        result.put("dislikesCount", postResponse.dislikesCount());
        return result;
    }

    @PostMapping("/post/dislikes/{postId}")
    @ResponseBody
    public Map<String, Object> toggleDislike(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable Long postId
    ) {
        var postResponse = postService.toggleDislike(postId, githubUser.unchangeableId());
        Map<String, Object> result = new HashMap<>();
        result.put("isLiking", postResponse.isLiking());
        result.put("likesCount", postResponse.likesCount());
        result.put("isDisliking", postResponse.isDisliking());
        result.put("dislikesCount", postResponse.dislikesCount());
        return result;
    }



    // 기본 post
    private PostResponse defaultPost(GithubUser githubUser) {
        // 만약, 로그인된 상태라면
        if (githubUser != null) {
            return new PostResponse(null, null, null, null,
                    0L, 0L, 0L, null, null,
                    githubUser.getName(), githubUser.unchangeableId(),null, null);
        } else { // 로그인 인한 상태
            return new PostResponse(null, null, null, null,
                    0L, 0L, 0L, null, null,
                    null, null, null, null);
        }

    }
}
