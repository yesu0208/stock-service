package arile.toy.stock_service.dto.response.post;

import arile.toy.stock_service.dto.postdto.ReplyDto;

import java.time.LocalDateTime;

public record ReplyResponse(
        Long replyId,
        String body,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String name,
        String unchangeableId
) {
    // Dto -> response
    public static ReplyResponse fromDto(ReplyDto dto) {
        return new ReplyResponse(
                dto.replyId(),
                dto.body(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.name(),
                dto.unchangeableId()
        );
    }
}