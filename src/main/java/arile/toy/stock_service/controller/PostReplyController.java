package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.PostRequest;
import arile.toy.stock_service.dto.request.ReplyRequest;
import arile.toy.stock_service.dto.response.PostResponse;
import arile.toy.stock_service.dto.response.ReplyResponse;
import arile.toy.stock_service.dto.response.SimplePostResponse;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.service.PostService;
import arile.toy.stock_service.service.ReplyService;
import arile.toy.stock_service.service.StaticStockInfoService;
import lombok.RequiredArgsConstructor;
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
public class PostReplyController {

    private final PostService postService;
    private final ReplyService replyService;
    private final StaticStockInfoService stockInfoService;

    // 단일 post 조회
    @GetMapping("/post")
    public String post(@AuthenticationPrincipal GithubUser githubUser,
                        @RequestParam(required = false) Long postId, // postId 들어가면 해당 내용, 아니면 빈 내용 보여주기
                       Model model) {

        List<String> stockNames = stockInfoService.loadStockNameList();

        // postId x : sample post, postId : 해당 postId의 post 조회
        PostResponse post = (postId != null) ?
                postService.loadPost(githubUser.unchangeableId(), postId) : defaultPost(githubUser);

        // postId x : sample reply, postId : 해당 postId의 reply 조회
        List<ReplyResponse> replies = (postId != null) ?
                replyService.loadAllRepliesByPostId(postId)
                        .stream()
                        .map(ReplyResponse::fromDto)
                        .toList(): null;

        model.addAttribute("stockNames", stockNames);
        model.addAttribute("post", post); // 여기에 게시물의 unchangeableId가 포함되어 있음.
        model.addAttribute("replies", replies);
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
        List<SimplePostResponse> posts = postService.loadAllMySimplePosts(githubUser.unchangeableId())
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
            @RequestParam(required = false) Long postId,
            PostRequest postRequest, // 폼 data로 받음
            RedirectAttributes redirectAttrs // redirection할 때, 생성/수정한 interest-group을 화면에서 유지하고 싶다
    ) {

        Long upsertPostId = postService.upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId);

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

    // post 좋아요
    @PostMapping("/post/likes/{postId}")
    @ResponseBody // ajax 사용을 위해 폼이 아닌 Json으로 전달
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


    // post 싫어요
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


    // reply 생성/수정
    @PostMapping("/reply/upsert/{postId}")
    public String createOrUpdateMyReply(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable Long postId,
            @RequestParam(required = false) Long replyId,
            ReplyRequest replyRequest, // 폼 data로 받음
            RedirectAttributes redirectAttrs // redirection할 때, 생성/수정한 interest-group을 화면에서 유지하고 싶다
    ) {

        replyService.upsertReply(replyRequest.toDto(githubUser.name(), githubUser.unchangeableId()),
                postId, replyId);

        redirectAttrs.addAttribute("postId", postId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    // reply 삭제
    @PostMapping("/reply/delete/{postId}")
    public String deleteMyReply(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable Long postId,
            @RequestParam(required = false) Long replyId,
            RedirectAttributes redirectAttrs
    ){
        replyService.deleteReply(githubUser.unchangeableId(), postId, replyId);
        redirectAttrs.addAttribute("postId", postId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
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
