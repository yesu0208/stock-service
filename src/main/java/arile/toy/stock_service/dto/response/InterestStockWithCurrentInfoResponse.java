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
        Integer changeValue
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
                dto.changeValue()
        );
    }
}