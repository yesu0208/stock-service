package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record InterestGroupDto(
        Long id,
        String groupName,
        String unchangeableId,
        Set<InterestStockDto> interestStocks,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    // Entity -> Dto
    public static InterestGroupDto fromEntity(InterestGroup entity) {
        return new InterestGroupDto(
                entity.getId(),
                entity.getGroupName(),
                entity.getUnchangeableId(),
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
            String unchangeableId,
            Set<InterestStockDto> interestStocks,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestGroupDto(id, groupName, unchangeableId, interestStocks, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestGroupDto of(
            String groupName,
            String unchangeableId,
            Set<InterestStockDto> interestStocks
    ) {
        return new InterestGroupDto(null, groupName, unchangeableId, interestStocks,null, null, null, null);
    }

    // Dto -> Entity
    public InterestGroup createEntity() {
        InterestGroup entity = InterestGroup.of(groupName, unchangeableId);
        entity.addInterestStocks(interestStocks.stream().map(InterestStockDto::createEntity).toList()); // Collection을 받을 수 있도록 설계 .toSet()이 없으니, .toList()를 사용

        return entity;
    }

    // Dto -> Entity
    public InterestGroup updateEntity(InterestGroup entity) {
        if (groupName != null) entity.setGroupName(groupName); // null로 요청이 들어오면 무시
        if (unchangeableId != null) entity.setUnchangeableId(unchangeableId);
        if (interestStocks != null) { // 아무것도 없으면 무시
            entity.clearInterestStocks();
            entity.addInterestStocks(interestStocks.stream().map(InterestStockDto::createEntity).toList());
        }

        return entity;
    }
}
