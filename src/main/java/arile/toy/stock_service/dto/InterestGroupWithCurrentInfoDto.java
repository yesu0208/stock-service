package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestStock;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public record InterestGroupWithCurrentInfoDto(
        Long id,
        String groupName,
        String userId,
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
}
