package arile.toy.stock_service.dto.request;

import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.dto.InterestStockDto;

import java.util.Set;

public record InterestGroupRequest(
        String groupName,
        Set<InterestStockDto> interestStocks
) {
    // static method
    public static InterestGroupRequest of(
            String groupName,
            Set<InterestStockDto> interestStocks
    ) {
        return new InterestGroupRequest(groupName, interestStocks);
    }

    // request -> Dto
    public InterestGroupDto toDto(String userId) {
        return InterestGroupDto.of(
                this.groupName,
                userId,
                this.interestStocks
        );
    }

    // Dto -> request는 필요 x (request는 주는 것은 아니므로)
}
