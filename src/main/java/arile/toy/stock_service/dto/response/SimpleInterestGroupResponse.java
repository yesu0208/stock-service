package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.InterestGroupDto;

import java.time.ZonedDateTime;

public record SimpleInterestGroupResponse(
        String groupName,
        String unchangeableId,
        ZonedDateTime createdAt,
        ZonedDateTime modifiedAt
) {
    // Dto -> response
    public static SimpleInterestGroupResponse fromDto(InterestGroupDto dto) {
        return new SimpleInterestGroupResponse(dto.groupName(), dto.unchangeableId(), dto.createdAt(), dto.modifiedAt());
    }
}
