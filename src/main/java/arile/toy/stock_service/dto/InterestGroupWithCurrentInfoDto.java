package arile.toy.stock_service.dto;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;

public record InterestGroupWithCurrentInfoDto(
        Long id,
        String groupName,
        String unchangeableId,
        Set<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos,

        ZonedDateTime createdAt,
        String createdBy,
        ZonedDateTime modifiedAt,
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
            Long id,
            String groupName,
            String unchangeableId,
            Set<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos,

            ZonedDateTime createdAt,
            String createdBy,
            ZonedDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestGroupWithCurrentInfoDto(id, groupName, unchangeableId, interestStockWithCurrentInfoDtos,createdAt,createdBy, modifiedAt, modifiedBy);
    }
}
