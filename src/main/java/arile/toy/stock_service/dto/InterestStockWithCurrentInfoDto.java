package arile.toy.stock_service.dto;

import java.time.LocalDateTime;

public record InterestStockWithCurrentInfoDto(
        Long id,
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
        String rateOfReturnString, // 수익률(String)

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    // static method (전체)
    public static InterestStockWithCurrentInfoDto of(
            Long id,
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer totalBuyingPrice,
            Integer fieldOrder,

            Integer nowValue,
            Integer changeValue,
            String changeRateString,
            Integer valuation,
            Integer unrealizedPL,
            Integer realizedPL,
            String rateOfReturnString,

            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestStockWithCurrentInfoDto(id, stockName, buyingPrice, numOfStocks,breakEvenPrice,totalBuyingPrice, fieldOrder, nowValue, changeValue, changeRateString, valuation, unrealizedPL, realizedPL, rateOfReturnString, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestStockWithCurrentInfoDto of(
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer totalBuyingPrice,
            Integer fieldOrder,

            Integer nowValue,
            Integer changeValue,
            String changeRateString,
            Integer valuation,
            Integer unrealizedPL,
            Integer realizedPL,
            String rateOfReturnString
    ) {
        return new InterestStockWithCurrentInfoDto(null, stockName, buyingPrice, numOfStocks, breakEvenPrice, totalBuyingPrice, fieldOrder, nowValue, changeValue, changeRateString, null, null, null, null, null, null, null, null);
    }
}
