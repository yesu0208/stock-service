package arile.toy.stock_service.dto.response;


import arile.toy.stock_service.dto.InterestStockDto;

public record InterestStockResponse(
        String stockName,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer fieldOrder
) {
    // Dto -> response
    public static InterestStockResponse fromDto(InterestStockDto dto) {
        return new InterestStockResponse(
                dto.stockInfo().getStockName(),
                dto.buyingPrice(),
                dto.numOfStocks(),
                dto.breakEvenPrice(),
                dto.fieldOrder()
        );
    }
}