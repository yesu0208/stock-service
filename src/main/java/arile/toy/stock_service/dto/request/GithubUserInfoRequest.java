package arile.toy.stock_service.dto.request;


import arile.toy.stock_service.dto.GithubUserInfoDto;

public record GithubUserInfoRequest(
        Double fee
) {
    // static method
    public static GithubUserInfoRequest of(
            Double fee
    ) {
        return new GithubUserInfoRequest(fee);
    }

    // request -> Dto (fee 변경할 떄 사용)
    public GithubUserInfoDto toDto() {
        return GithubUserInfoDto.of(this.fee());
    }

    // Dto -> request는 필요 x (request는 주는 것은 아니므로)
}
