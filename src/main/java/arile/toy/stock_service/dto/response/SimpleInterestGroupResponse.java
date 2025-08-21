package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.InterestGroupDto;

import java.time.LocalDateTime;

public record SimpleInterestGroupResponse(
        String groupName,
        String userId,
        LocalDateTime modifiedAt
) {
    // Dto -> response
    public static SimpleInterestGroupResponse fromDto(InterestGroupDto dto) {
        return new SimpleInterestGroupResponse(dto.groupName(), dto.userId(), dto.modifiedAt());
    }
}
