package arile.toy.stock_service.dto.response.interest;

import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;

import java.util.List;

public record InterestGroupWithCurrentInfoResponse(
        String groupName,
        String unchangeableId,
        List<InterestStockWithCurrentInfoResponse> interestStocks
) {
    // Dto -> response
    public static InterestGroupWithCurrentInfoResponse fromDto(InterestGroupWithCurrentInfoDto dto) {
        return new InterestGroupWithCurrentInfoResponse(
                dto.groupName(),
                dto.unchangeableId(),
                dto.interestStockWithCurrentInfoDtos().stream().map(InterestStockWithCurrentInfoResponse::fromDto).toList()
        );
    }
}


