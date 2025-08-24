package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.InterestStockWithCurrentInfoDto;

public record InterestStockWithCurrentInfoResponse(
        String stockName,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer totalBuyingPrice,
        Integer fieldOrder,
        Integer nowValue,
        Integer changeValue,
        String changeRateString,
        Integer valuation, // 평가금액
        Integer unrealizedPL, // 평가손익
        Integer realizedPL, // 실현손익
        String rateOfReturnString // 수익률(String)
) {
    // Dto -> response
    public static InterestStockWithCurrentInfoResponse fromDto(InterestStockWithCurrentInfoDto dto) {
        return new InterestStockWithCurrentInfoResponse(
                dto.stockName(),
                dto.buyingPrice(),
                dto.numOfStocks(),
                dto.breakEvenPrice(),
                dto.totalBuyingPrice(),
                dto.fieldOrder(),
                dto.nowValue(),
                dto.changeValue(),
                dto.changeRateString(),
                dto.valuation(),
                dto.unrealizedPL(),
                dto.realizedPL(),
                dto.rateOfReturnString()
        );
    }
}