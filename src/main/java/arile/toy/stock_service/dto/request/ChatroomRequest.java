package arile.toy.stock_service.dto.request;

public record ChatroomRequest(
        String title,
        String stockName,
        String createdBy
) {
}
