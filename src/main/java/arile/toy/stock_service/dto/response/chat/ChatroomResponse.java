package arile.toy.stock_service.dto.response.chat;

import arile.toy.stock_service.dto.chatdto.ChatroomDto;

import java.time.LocalDateTime;

public record ChatroomResponse(
        Long chatroomId,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        String stockName,
        String createdBy,
        String unchangeableId,
        Boolean hasNewMessage
) {
    // Dto -> response
    public static ChatroomResponse fromDto(ChatroomDto dto) {
        return new ChatroomResponse(
                dto.chatroomId(),
                dto.title(),
                dto.memberCount(),
                dto.createdAt(),
                dto.stockName(),
                dto.createdBy(),
                dto.unchangeableId(),
                dto.hasNewMessage()
        );
    }
}
