package arile.toy.stock_service.domain.naverstock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NxtOverMarketPriceInfo(
        String tradingSessionType,
        String overMarketStatus,
        String overPrice,
        CompareToPreviousPrice compareToPreviousPrice,
        String compareToPreviousClosePrice,
        String fluctuationsRatio,
        String localTradedAt,
        TradeStopType tradeStopType
) {
}
