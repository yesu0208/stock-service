package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.SimplePostDto;

import java.time.ZonedDateTime;

public record SimplePostResponse(
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
    // Dto -> response
    public static SimplePostResponse fromDto(SimplePostDto dto) {
        return new SimplePostResponse(
                dto.postId(),
                dto.title(),
                dto.stockName(),
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
