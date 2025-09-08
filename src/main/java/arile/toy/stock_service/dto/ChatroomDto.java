package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.Chatroom;

import java.time.LocalDateTime;

public record ChatroomDto(
        Long chatroomId,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        String stockName,
        String createdBy,
        String unchangeableId,
        Boolean hasNewMessage
) {
    // Entity -> Dto
    public static ChatroomDto fromEntity(Chatroom entity) {
        return new ChatroomDto(
                entity.getChatroomId(),
                entity.getTitle(),
                entity.getGithubUserChatroomMappings().size(),
                entity.getCreatedAt(),
                entity.getStockName(),
                entity.getCreatedBy(),
                entity.getUnchangeableId(),
                entity.getHasNewMessage()
        );
    }

    // static method
    public static ChatroomDto of(
            Long chatroomId,
            String title,
            Integer memberCount,
            LocalDateTime createdAt,
            String stockName,
            String createdBy,
            String unchangeableId,
            Boolean hasNewMessage
    ){
        return new ChatroomDto(chatroomId, title, memberCount, createdAt, stockName, createdBy, unchangeableId, hasNewMessage);
    }
}
