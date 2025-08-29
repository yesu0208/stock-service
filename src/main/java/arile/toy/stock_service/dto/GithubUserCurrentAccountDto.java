package arile.toy.stock_service.dto;

public record GithubUserCurrentAccountDto(
        String unchangeableId,
        Integer totalBuyingPrice,
        Integer totalValuation,
        Integer totalUnrealizedPL,
        Integer totalRealizedPL,
        String rateOfReturnString

) {
    // static method (전체)
    public static GithubUserCurrentAccountDto of(
            String unchangeableId,
            Integer totalBuyingPrice,
            Integer totalValuation,
            Integer totalUnrealizedPL,
            Integer totalRealizedPL,
            String rateOfReturnString
    ) {
        return new GithubUserCurrentAccountDto(unchangeableId, totalBuyingPrice, totalValuation,
                totalUnrealizedPL, totalRealizedPL, rateOfReturnString);
    }

}
