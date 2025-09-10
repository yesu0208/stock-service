package arile.toy.stock_service.dto.request.post;

import arile.toy.stock_service.dto.postdto.PostDto;

public record PostRequest(
        String title,
        String stockName,
        String body,
        String name,
        String unchangeableId
) {
    // request -> Dto
    public PostDto toDto(String name, String unchangeableId) {
        return PostDto.of(title, stockName, body, name, unchangeableId);
    }

    // static method
    public static PostRequest of(
            String title,
            String stockName,
            String body,
            String name,
            String unchangeableId
    ) {
        return new PostRequest(
                title,
                stockName,
                body,
                name,
                unchangeableId
        );
    }
}
