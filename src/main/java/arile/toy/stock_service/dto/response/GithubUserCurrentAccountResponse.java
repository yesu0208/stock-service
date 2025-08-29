package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.GithubUserCurrentAccountDto;

public record GithubUserCurrentAccountResponse(
        String unchangeableId,
        Integer totalBuyingPrice,
        Integer totalValuation,
        Integer totalUnrealizedPL,
        Integer totalRealizedPL,
        String rateOfReturnString

) {
    // Dto -> response
    public static GithubUserCurrentAccountResponse fromDto(GithubUserCurrentAccountDto dto) {
        return new GithubUserCurrentAccountResponse(
                dto.unchangeableId(),
                dto.totalBuyingPrice(),
                dto.totalValuation(),
                dto.totalUnrealizedPL(),
                dto.totalRealizedPL(),
                dto.rateOfReturnString()
        );
    }

}
