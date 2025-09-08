package arile.toy.stock_service.dto.request;


import arile.toy.stock_service.dto.GithubUserInfoDto;

public record GithubUserInfoRequest(
        Double feeRate
) {
    // static method
    public static GithubUserInfoRequest of(
            Double feeRate
    ) {
        return new GithubUserInfoRequest(feeRate);
    }

}
