package arile.toy.stock_service.dto.request;

import arile.toy.stock_service.dto.ReplyDto;

public record ReplyRequest(
        String body,
        String name,
        String unchangeableId
) {
    // request -> Dto
    public ReplyDto toDto(String name, String unchangeableId) {
        return ReplyDto.of(body, name, unchangeableId);
    }

    // static method
    public static ReplyRequest of(
            String body,
            String name,
            String unchangeableId
    ) {
        return new ReplyRequest(
                body,
                name,
                unchangeableId
        );
    }
}
