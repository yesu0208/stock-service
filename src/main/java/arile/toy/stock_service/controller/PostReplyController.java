package arile.toy.stock_service.controller;

import arile.toy.stock_service.dto.request.post.PostRequest;
import arile.toy.stock_service.dto.request.post.ReplyRequest;
import arile.toy.stock_service.dto.response.post.PostResponse;
import arile.toy.stock_service.dto.response.post.ReplyResponse;
import arile.toy.stock_service.dto.response.post.SimplePostResponse;
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

    @GetMapping("/post")
    public String getPost(@AuthenticationPrincipal GithubUser githubUser,
                        @RequestParam(required = false) Long postId,
                       Model model) {

        List<String> stockNames = stockInfoService.loadStockNameList();

        // postId(x) : sample post, postId(o) : 해당 postId의 post 조회
        PostResponse post = (postId != null) ?
                PostResponse.fromDto(postService.loadPost(githubUser.unchangeableId(), postId)) : defaultPost(githubUser);

        // postId(x) : sample reply(null), postId(o) : 해당 postId의 reply 조회
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

    // 모든 post 목록 조회 (내용 제외한 SimplePostResponse)
    @GetMapping("/posts")
    public String getAllSimplePostList(Model model) {

        List<SimplePostResponse> posts = postService.loadAllSimplePosts()
                        .stream()
                        .map(SimplePostResponse::fromDto)
                        .toList();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/my-posts")
    public String getAllMySimplePostList(@AuthenticationPrincipal GithubUser githubUser,
                                    Model model) {

        List<SimplePostResponse> posts = postService.loadAllMySimplePosts(githubUser.unchangeableId())
                .stream()
                .map(SimplePostResponse::fromDto)
                .toList();
        model.addAttribute("posts", posts);
        return "my-posts";
    }

    @PostMapping("/post")
    public String createOrUpdatePost(@AuthenticationPrincipal GithubUser githubUser,
                                       @RequestParam(required = false) Long postId,
                                       PostRequest postRequest,
                                       RedirectAttributes redirectAttrs) {

        Long upsertPostId = postService.upsertPost(postRequest.toDto(githubUser.name(), githubUser.unchangeableId()), postId);

        redirectAttrs.addAttribute("postId", upsertPostId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    @PostMapping("/my-posts/{postId}")
    public String deletePost(@AuthenticationPrincipal GithubUser githubUser,
                             @PathVariable Long postId){

        postService.deletePost(githubUser.unchangeableId(), postId);
        return "redirect:/my-posts"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    @PostMapping("/post/likes/{postId}")
    @ResponseBody
    public Map<String, Object> toggleLike(@AuthenticationPrincipal GithubUser githubUser,
                                          @PathVariable Long postId) {
        var postResponse = PostResponse.fromDto(postService.toggleLike(githubUser.unchangeableId(), postId));
        Map<String, Object> result = new HashMap<>();
        result.put("isLiking", postResponse.isLiking());
        result.put("likesCount", postResponse.likesCount());
        result.put("isDisliking", postResponse.isDisliking());
        result.put("dislikesCount", postResponse.dislikesCount());
        return result;
    }


    @PostMapping("/post/dislikes/{postId}")
    @ResponseBody
    public Map<String, Object> toggleDislike(@AuthenticationPrincipal GithubUser githubUser,
                                             @PathVariable Long postId) {

        var postResponse = PostResponse.fromDto(postService.toggleDislike(githubUser.unchangeableId(), postId));
        Map<String, Object> result = new HashMap<>();
        result.put("isLiking", postResponse.isLiking());
        result.put("likesCount", postResponse.likesCount());
        result.put("isDisliking", postResponse.isDisliking());
        result.put("dislikesCount", postResponse.dislikesCount());
        return result;
    }


    @PostMapping("/reply/upsert/{postId}")
    public String createOrUpdateReply(@AuthenticationPrincipal GithubUser githubUser,
                                      @PathVariable Long postId,
                                      @RequestParam(required = false) Long replyId,
                                      ReplyRequest replyRequest,
                                      RedirectAttributes redirectAttrs) {

        replyService.upsertReply(replyRequest.toDto(githubUser.name(), githubUser.unchangeableId()),
                postId, replyId);

        redirectAttrs.addAttribute("postId", postId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    @PostMapping("/reply/delete/{postId}")
    public String deleteReply(@AuthenticationPrincipal GithubUser githubUser,
                              @PathVariable Long postId,
                              @RequestParam(required = false) Long replyId,
                              RedirectAttributes redirectAttrs){

        replyService.deleteReply(githubUser.unchangeableId(), postId, replyId);
        redirectAttrs.addAttribute("postId", postId); // GET에 postId 전달

        return "redirect:/post"; // redirection : PRG pattern (POST REDIRECT GET)
    }


    private PostResponse defaultPost(GithubUser githubUser) {

        return new PostResponse(null, null, null, null,
                    0L, 0L, 0L, null, null,
                    githubUser.getName(), githubUser.unchangeableId(),null, null);

    }
}
