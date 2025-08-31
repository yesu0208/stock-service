package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.ReplyDto;

import java.time.ZonedDateTime;

public record ReplyResponse(
        Long replyId,
        String body,
        ZonedDateTime createdAt,
        ZonedDateTime modifiedAt,
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