package arile.toy.stock_service.dto.response.interest;


import arile.toy.stock_service.dto.interestdto.InterestStockDto;

public record InterestStockResponse(
        String stockName,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer totalBuyingPrice,
        Integer fieldOrder
) {
    // Dto -> response
    public static InterestStockResponse fromDto(InterestStockDto dto) {
        return new InterestStockResponse(
                dto.stockName(),
                dto.buyingPrice(),
                dto.numOfStocks(),
                dto.breakEvenPrice(),
                dto.totalBuyingPrice(),
                dto.fieldOrder()
        );
    }
}