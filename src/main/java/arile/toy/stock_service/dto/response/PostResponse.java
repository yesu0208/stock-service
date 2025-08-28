package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.PostDto;
import arile.toy.stock_service.dto.SimplePostDto;

import java.time.LocalDateTime;

public record PostResponse(
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
    // Dto -> response
    public static PostResponse fromDto(PostDto dto) {
        return new PostResponse(
                dto.postId(),
                dto.title(),
                dto.stockName(),
                dto.body(),
                dto.repliesCount(),
                dto.likesCount(),
                dto.dislikesCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.name(),
                dto.unchangeableId()
        );
    }

    // Dto(Simple) -> response
    public static PostResponse fromDto(SimplePostDto dto) {
        return new PostResponse(
                dto.postId(),
                dto.title(),
                dto.stockName(),
                null,
                dto.repliesCount(),
                dto.likesCount(),
                dto.dislikesCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.name(),
                dto.unchangeableId()
        );
    }
}