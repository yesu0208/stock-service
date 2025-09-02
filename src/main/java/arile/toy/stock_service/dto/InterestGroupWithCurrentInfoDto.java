package arile.toy.stock_service.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public record InterestGroupWithCurrentInfoDto(
        Long interestGroupId,
        String groupName,
        String unchangeableId,
        Set<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public void addInterestStockWithCurrentInfoDtos(Collection<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos) {
        interestStockWithCurrentInfoDtos.forEach(this::addInterestStockWithCurrentInfoDto);
    }

    public void addInterestStockWithCurrentInfoDto(InterestStockWithCurrentInfoDto interestStockWithCurrentInfoDto) {
        interestStockWithCurrentInfoDtos.add(interestStockWithCurrentInfoDto);
    }

    // static method (전체)
    public static InterestGroupWithCurrentInfoDto of(
            Long interestGroupId,
            String groupName,
            String unchangeableId,
            Set<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos,

            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestGroupWithCurrentInfoDto(interestGroupId, groupName, unchangeableId, interestStockWithCurrentInfoDtos,createdAt,createdBy, modifiedAt, modifiedBy);
    }
}
