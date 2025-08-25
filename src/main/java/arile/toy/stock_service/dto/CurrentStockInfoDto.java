package arile.toy.stock_service.dto;

import java.time.LocalDateTime;

public record CurrentStockInfoDto(
        String shortCode,
        String stockName,

        String marketState, // 장 시작, 종료 여부
        String marketType, // 시장 종류
        Integer nowValue, // 현재가
        Integer changeValue, // 전일대비 상승가격
        Double changeRate, // 전일배비 상승률
        String riseOrFall, // 1 : 상한가, 2 : 상승, 3 : 보합, 4 : 하한가, 5 : 하락
        Integer highestValue, // 상한가
        Integer lowestValue, // 하한가
        Integer upperLimit, // 고가
        Integer lowerLimit, // 저가
        Integer openValue, // 시가
        Integer standardValue, // 기준가
        Long transactionVolume, // 거래량
        Long transactionValue,  // 거래대금
        LocalDateTime time // 기준 시각
) {
    // static method (전체)
    public static CurrentStockInfoDto of(
            String shortCode,
            String stockName,

            String marketState,
            String marketType,
            Integer nowValue,
            Integer changeValue,
            Double changeRate,
            String riseOrFall,
            Integer highestValue,
            Integer lowestValue,
            Integer upperLimit,
            Integer lowerLimit,
            Integer openValue,
            Integer standardValue,
            Long transactionVolume,
            Long transactionValue,
            LocalDateTime time
    ) {
        return new CurrentStockInfoDto(shortCode, stockName, marketState, marketType, nowValue,
                changeValue, changeRate, riseOrFall, highestValue, lowestValue, upperLimit, lowerLimit,
                openValue, standardValue, transactionVolume, transactionValue, time);
    }
}
