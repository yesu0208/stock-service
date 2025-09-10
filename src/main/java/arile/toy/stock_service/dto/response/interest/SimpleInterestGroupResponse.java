package arile.toy.stock_service.dto.response.interest;

import arile.toy.stock_service.dto.interestdto.InterestGroupDto;

import java.time.LocalDateTime;

public record SimpleInterestGroupResponse(
        String groupName,
        String unchangeableId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    // Dto -> response
    public static SimpleInterestGroupResponse fromDto(InterestGroupDto dto) {
        return new SimpleInterestGroupResponse(dto.groupName(), dto.unchangeableId(), dto.createdAt(), dto.modifiedAt());
    }
}
