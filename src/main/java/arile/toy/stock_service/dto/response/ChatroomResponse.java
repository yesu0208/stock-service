package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.ChatroomDto;
import arile.toy.stock_service.dto.CurrentStockInfoDto;

import java.time.LocalDateTime;

public record ChatroomResponse(
        Long chatroomId,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        Boolean hasNewMessage
) {
    // Dto -> response
    public static ChatroomResponse fromDto(ChatroomDto dto) {
        return new ChatroomResponse(
                dto.chatroomId(),
                dto.title(),
                dto.memberCount(),
                dto.createdAt(),
                dto.hasNewMessage()
        );
    }
}
