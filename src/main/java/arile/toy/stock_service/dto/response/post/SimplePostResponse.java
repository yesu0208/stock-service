package arile.toy.stock_service.dto.response.post;

import arile.toy.stock_service.dto.postdto.SimplePostDto;

import java.time.LocalDateTime;

public record SimplePostResponse(
        Long postId,
        String title,
        String stockName,
        Long repliesCount,
        Long likesCount,
        Long dislikesCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
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
