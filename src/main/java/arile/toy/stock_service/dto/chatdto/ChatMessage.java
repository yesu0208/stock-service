package arile.toy.stock_service.dto.chatdto;

import arile.toy.stock_service.domain.constant.MessageType;

import java.time.LocalDateTime;

public record ChatMessage(
        String sender,
        String message,
        LocalDateTime createdAt,
        MessageType messageType,
        String unchangeableId
) {
}
