package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.InterestGroupDto;

import java.util.List;

public record InterestGroupResponse(
        String groupName,
        String unchangeableId,
        List<InterestStockResponse> interestStocks
) {
    // Dto -> response
    public static InterestGroupResponse fromDto(InterestGroupDto dto) {
        return new InterestGroupResponse(
                dto.groupName(),
                dto.unchangeableId(),
                dto.interestStocks().stream().map(InterestStockResponse::fromDto).toList()
        );
    }
}


