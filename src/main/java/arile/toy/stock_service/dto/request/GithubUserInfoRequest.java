package arile.toy.stock_service.dto.request;


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
