package arile.toy.stock_service.dto.request;

import arile.toy.stock_service.dto.InterestGroupDto;

import java.util.List;
import java.util.stream.Collectors;

public record InterestGroupRequest(
        String groupName,
        List<InterestStockRequest> interestStocks
) {
    // static method
    public static InterestGroupRequest of(
            String groupName,
            List<InterestStockRequest> interestStocks
    ) {
        return new InterestGroupRequest(groupName, interestStocks);
    }

    // request -> Dto
    public InterestGroupDto toDto(String unchangeableId, Double fee) {
        return InterestGroupDto.of(
                groupName(),
                unchangeableId,
                interestStocks.stream()
                        .map(each -> each.toDto(fee))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    // Dto -> request는 필요 x (request는 주는 것은 아니므로)
}
