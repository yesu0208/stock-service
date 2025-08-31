package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.GithubUserInfoDto;

import java.time.ZonedDateTime;

public record GithubUserInfoResponse(
        String unchangeableId,
        String id,
        String name,
        String email,
        ZonedDateTime lastLoginAt,
        Double fee
) {
    // Dto -> response
    public static GithubUserInfoResponse fromDto(GithubUserInfoDto dto) {
        return new GithubUserInfoResponse(
                dto.unchangeableId(),
                dto.id(),
                dto.name(),
                dto.email(),
                dto.lastLoginAt(),
                dto.fee()
        );
    }
}
