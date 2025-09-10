package arile.toy.stock_service.dto.request.interest;

import arile.toy.stock_service.dto.interestdto.InterestGroupDto;

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

}
