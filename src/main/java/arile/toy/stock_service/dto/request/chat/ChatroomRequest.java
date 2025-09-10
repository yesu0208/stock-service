package arile.toy.stock_service.dto.request.chat;

public record ChatroomRequest(
        String title,
        String stockName,
        String createdBy
) {
    // static method
    public static ChatroomRequest of(
            String title,
            String stockName,
            String createdBy
    ) {
        return new ChatroomRequest(title, stockName, createdBy);
    }
}
