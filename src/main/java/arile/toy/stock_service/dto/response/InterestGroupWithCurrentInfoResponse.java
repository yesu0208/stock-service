package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;

import java.util.List;

public record InterestGroupWithCurrentInfoResponse(
        String groupName,
        String userId,
        List<InterestStockWithCurrentInfoResponse> interestStocks
) {
    // Dto -> response
    public static InterestGroupWithCurrentInfoResponse fromDto(InterestGroupWithCurrentInfoDto dto) {
        return new InterestGroupWithCurrentInfoResponse(
                dto.groupName(),
                dto.userId(),
                dto.interestStockWithCurrentInfoDtos().stream().map(InterestStockWithCurrentInfoResponse::fromDto).toList()
        );
    }
}


