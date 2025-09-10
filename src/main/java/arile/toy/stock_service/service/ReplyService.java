package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.post.Reply;
import arile.toy.stock_service.dto.postdto.ReplyDto;
import arile.toy.stock_service.exception.post.PostNotFoundException;
import arile.toy.stock_service.exception.reply.ReplyNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.PostRepository;
import arile.toy.stock_service.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final GithubUserInfoRepository githubUserInfoRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<ReplyDto> loadAllRepliesByPostId(Long postId) {
        return replyRepository.findAllByPostPostId(postId)
                .stream()
                .map(ReplyDto::fromEntity)
                .toList();
    }

    public void upsertReply(ReplyDto dto, Long postId, Long replyId){

        GithubUserInfo user = githubUserInfoRepository.findById(dto.unchangeableId())
                .orElseThrow(() -> new UserNotFoundException(dto.unchangeableId()));

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        replyRepository.findByUserUnchangeableIdAndPostPostIdAndReplyId(dto.unchangeableId(), postId, replyId)
                .ifPresentOrElse( // Optional
                        entity -> replyRepository.save(dto.updateEntity(entity)),
                        () -> replyRepository.save(dto.createEntity(user, post))
                );
    }

    public void deleteReply(String unchangeableId, Long postId, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));
        var post = reply.getPost();
        post.setRepliesCount(post.getRepliesCount() -1 );
        replyRepository.deleteByUserUnchangeableIdAndPostPostIdAndReplyId(unchangeableId, postId, replyId);
    }

}
