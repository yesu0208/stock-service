package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.post.Post;

import java.time.ZonedDateTime;

public record SimplePostDto(
        Long postId,
        String title,
        String stockName,
        Long repliesCount,
        Long likesCount,
        Long dislikesCount,
        ZonedDateTime createdAt,
        ZonedDateTime modifiedAt,
        String name,
        String unchangeableId
) {
    // Entity -> Dto
    public static SimplePostDto fromEntity(Post entity) {
        return new SimplePostDto(
                entity.getPostId(),
                entity.getTitle(),
                entity.getStockName(),
                entity.getRepliesCount(),
                entity.getLikesCount(),
                entity.getDislikesCount(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getUser().getName(),
                entity.getUser().getUnchangeableId()
        );
    }
}
