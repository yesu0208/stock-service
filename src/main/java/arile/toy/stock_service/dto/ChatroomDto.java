package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.Chatroom;

import java.time.LocalDateTime;

public record ChatroomDto(
        Long chatroomId,
        String title,
        Integer memberCount,
        LocalDateTime createdAt
) {
    // Entity -> Dto
    public static ChatroomDto fromEntity(Chatroom entity) {
        return new ChatroomDto(
                entity.getChatroomId(),
                entity.getTitle(),
                entity.getGithubUserChatroomMappings().size(),
                entity.getCreatedAt()
        );
    }
}
