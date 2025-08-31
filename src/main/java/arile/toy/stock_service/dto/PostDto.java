package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.post.Post;

import java.time.LocalDateTime;

public record PostDto(
        Long postId,
        String title,
        String stockName,
        String body,
        Long repliesCount,
        Long likesCount,
        Long dislikesCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String name,
        String unchangeableId
) {
    // Entity -> Dto
    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getPostId(),
                entity.getTitle(),
                entity.getStockName(),
                entity.getBody(),
                entity.getRepliesCount(),
                entity.getLikesCount(),
                entity.getDislikesCount(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getUser().getName(),
                entity.getUser().getUnchangeableId()
        );
    }

    // static method (전체)
    public static PostDto of(
            Long postId,
            String title,
            String stockName,
            String body,
            Long repliesCount,
            Long likesCount,
            Long dislikesCount,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            String name,
            String unchangeableId
    ) {
        return new PostDto(postId, title, stockName, body, repliesCount, likesCount, dislikesCount, createdAt, modifiedAt, name, unchangeableId);
    }

    // static method (일부)
    public static PostDto of(String title, String stockName, String body, String name, String unchangeableId) {
        return new PostDto(null, title, stockName, body, null, null, null, null, null, name, unchangeableId);
    }


    // Dto -> Entity
    public Post createEntity(GithubUserInfo user) {
        return Post.of(title, stockName, body, 0L, 0L, 0L, user);
    }

    // Dto -> Entity
    public Post updateEntity(Post entity) {
        if (title != null) entity.setTitle(title); // null로 요청이 들어오면 무시
        entity.setStockName(stockName);
        entity.setBody(body);

        return entity;
    }
}