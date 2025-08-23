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

            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestStockWithCurrentInfoDto(id, stockName, buyingPrice, numOfStocks,breakEvenPrice,totalBuyingPrice, fieldOrder, nowValue, changeValue, changeRateString, createdAt, createdBy, modifiedAt, modifiedBy);
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
            String changeRateString
    ) {
        return new InterestStockWithCurrentInfoDto(null, stockName, buyingPrice, numOfStocks, breakEvenPrice, totalBuyingPrice, fieldOrder, nowValue, changeValue, changeRateString, null, null, null, null);
    }
}
