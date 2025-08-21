package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record InterestGroupDto(
        Long id,
        String groupName,
        String userId,
        Set<InterestStockDto> interestStocks,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAT,
        String modifiedBy
) {
    public static InterestGroupDto fromEntity(InterestGroup entity) {
        return new InterestGroupDto(
                entity.getId(),
                entity.getGroupName(),
                entity.getUserId(),
                entity.getInterestStocks().stream() // DTO로 변환
                        .map(InterestStockDto::fromEntity)
                        .collect(Collectors.toUnmodifiableSet()), // 불변성
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
