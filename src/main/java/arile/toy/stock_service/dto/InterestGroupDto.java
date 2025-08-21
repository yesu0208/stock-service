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

    // static method (전체)
    public static InterestGroupDto of(
            Long id,
            String groupName,
            String userId,
            Set<InterestStockDto> interestStocks,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestGroupDto(id, groupName, userId, interestStocks, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestGroupDto of(
            String groupName,
            String userId,
            Set<InterestStockDto> interestStocks
    ) {
        return new InterestGroupDto(null, groupName, userId, interestStocks,null, null, null, null);
    }
}
