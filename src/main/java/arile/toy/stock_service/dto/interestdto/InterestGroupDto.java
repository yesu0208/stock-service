package arile.toy.stock_service.dto.interestdto;

import arile.toy.stock_service.domain.interest.InterestGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record InterestGroupDto(
        Long interestGroupId,
        String groupName,
        String unchangeableId,
        Set<InterestStockDto> interestStockDtos,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    // Entity -> Dto
    public static InterestGroupDto fromEntity(InterestGroup entity) {
        return new InterestGroupDto(
                entity.getInterestGroupId(),
                entity.getGroupName(),
                entity.getUnchangeableId(),
                entity.getInterestStocks().stream()
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
            Long interestGroupId,
            String groupName,
            String unchangeableId,
            Set<InterestStockDto> interestStockDtos,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestGroupDto(interestGroupId, groupName, unchangeableId, interestStockDtos, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestGroupDto of(
            String groupName,
            String unchangeableId,
            Set<InterestStockDto> interestStockDtos
    ) {
        return new InterestGroupDto(null, groupName, unchangeableId, interestStockDtos,null, null, null, null);
    }

    // Dto -> Entity
    public InterestGroup createEntity() {
        InterestGroup entity = InterestGroup.of(groupName, unchangeableId);
        entity.addInterestStocks(interestStockDtos.stream().map(InterestStockDto::createEntity).toList());

        return entity;
    }

    // Dto -> Entity
    public InterestGroup updateEntity(InterestGroup entity) {
        if (groupName != null) entity.setGroupName(groupName); // null로 요청이 들어오면 무시
        if (unchangeableId != null) entity.setUnchangeableId(unchangeableId);
        if (interestStockDtos != null) { // 아무것도 없으면 무시
            entity.setModifiedAt(null); // AuditingFields의 @LastModifiedDate, @LastModifiedBy 정상작동 위해서 추가
            entity.clearInterestStocks();
            entity.addInterestStocks(interestStockDtos.stream().map(InterestStockDto::createEntity).toList());
        }

        return entity;
    }
}
