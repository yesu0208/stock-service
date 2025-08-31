package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.GithubUserInfo;

import java.time.LocalDateTime;

public record GithubUserInfoDto(
        String unchangeableId,
        String id,
        String name,
        String email,
        LocalDateTime lastLoginAt,
        Double fee
) {
    // Entity -> Dto
    public static GithubUserInfoDto fromEntity(GithubUserInfo entity) {
        return new GithubUserInfoDto(
                entity.getUnchangeableId(),
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLastLoginAt(),
                entity.getFee()
        );
    }

    // static method (전체)
    public static GithubUserInfoDto of(
            String unchangeableId,
            String id,
            String name,
            String email,
            LocalDateTime lastLoginAt,
            Double fee
    ) {
        return new GithubUserInfoDto(unchangeableId, id, name, email, lastLoginAt, fee);
    }

    // static method (일부)
    public static GithubUserInfoDto of(Double fee) {
        return new GithubUserInfoDto(null, null, null, null, null, fee/100); // 입력받은 fee는 %단위, 저장한 fee는 %단위 아니다.
    }

    // Dto -> Entity
    public GithubUserInfo updateEntityFee(GithubUserInfo entity, Double fee) {

        entity.setFee(fee);

        return entity;
    }
}
