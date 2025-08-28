package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.post.Post;
import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.dto.PostDto;
import arile.toy.stock_service.dto.SimplePostDto;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final GithubUserInfoRepository githubUserInfoRepository;

    @Transactional(readOnly = true)
    public PostDto loadPost(Long postId) {
        return postRepository.findById(postId)
                .map(PostDto::fromEntity)
                // Optional
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다 - postId: " + postId));
    }


    @Transactional(readOnly = true)
    public List<SimplePostDto> loadAllSimplePosts() {
        return postRepository.findAll()
                .stream()
                .map(SimplePostDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SimplePostDto> loadAllMySimplePosts(GithubUser githubUser) {
        return postRepository.findAllByUserUnchangeableId(githubUser.unchangeableId())
                .stream()
                .map(SimplePostDto::fromEntity)
                .toList();
    }


    public Long upsertPost(PostDto dto){

        GithubUserInfo user = githubUserInfoRepository.findById(dto.unchangeableId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다 - unchangeableId: " + dto.unchangeableId()));

        postRepository.findByUserUnchangeableIdAndTitleAndStockName(dto.unchangeableId(), dto.title(), dto.stockName())
                .ifPresentOrElse( // Optional
                        entity -> postRepository.save(dto.updateEntity(entity)),
                        () -> postRepository.save(dto.createEntity(user))
                );
        // upsertPostId
        return  postRepository.findByUserUnchangeableIdAndTitleAndStockName(dto.unchangeableId(), dto.title(), dto.stockName())
                .map(Post::getPostId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다 "));
    }

    public void deletePost(String unchangeableId, Long postId) {
        postRepository.deleteByUserUnchangeableIdAndPostId(unchangeableId, postId);
    }


}
