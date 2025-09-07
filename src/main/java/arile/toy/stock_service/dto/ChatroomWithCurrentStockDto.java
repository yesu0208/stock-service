package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.Chatroom;

import java.time.LocalDateTime;

public record ChatroomWithCurrentStockDto(
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
    // static method (전체)
    public static ChatroomWithCurrentStockDto of(
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
        return new ChatroomWithCurrentStockDto(chatroomId, title, memberCount, createdAt,
                stockName, createdBy, hasNewMessage, marketState, nowValue, changeValue, changeRate);
    }
}
