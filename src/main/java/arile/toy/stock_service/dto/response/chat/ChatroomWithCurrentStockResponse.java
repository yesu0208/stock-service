package arile.toy.stock_service.dto.response.chat;

import arile.toy.stock_service.dto.chatdto.ChatroomWithCurrentStockDto;

import java.time.LocalDateTime;

public record ChatroomWithCurrentStockResponse(
        Long chatroomId,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        String stockName,
        String createdBy,
        Boolean hasNewMessage,

        String marketState,
        Integer nowValue,
        Integer changeValue,
        Double changeRate

) {
    // Dto -> response
    public static ChatroomWithCurrentStockResponse fromDto(ChatroomWithCurrentStockDto dto) {
        return new ChatroomWithCurrentStockResponse(
                dto.chatroomId(),
                dto.title(),
                dto.memberCount(),
                dto.createdAt(),
                dto.stockName(),
                dto.createdBy(),
                dto.hasNewMessage(),
                dto.marketState(),
                dto.nowValue(),
                dto.changeValue(),
                dto.changeRate()
        );
    }
}
