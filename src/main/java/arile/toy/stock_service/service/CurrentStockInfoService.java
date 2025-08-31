package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.naverstock.NaverStockResponse;
import arile.toy.stock_service.dto.CurrentStockInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CurrentStockInfoService {

    private final RestClient restClient = RestClient.create();
    private final StockInfoService stockInfoService;

    public CurrentStockInfoDto getCurrentStockInfo(String stockName) {

        String shortCode = stockInfoService.loadShortCodeByStockName(stockName);

        String stringResponse = restClient
                .get()
                .uri("https://polling.finance.naver.com/api/realtime?query=SERVICE_ITEM:" + shortCode)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    // 외부 API 호출 예외 처리(실패할 경우)
                    throw new RuntimeException(); // 추후 예외 추가
                })
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        NaverStockResponse response;
        try {
            response = mapper.readValue(stringResponse, NaverStockResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // 추후 예외 추가
        }


        // response로부터 data 추출
        var stockData = response.result().areas().getFirst().datas().getFirst();

        String marketState = stockData.ms();
        String marketType = stockData.mt();
        Integer nowValue = stockData.nv();
        Integer changeValue = stockData.cv();
        Double changeRate = stockData.cr();
        String riseOrFall = stockData.rf();
        Integer highestValue = stockData.hv();
        Integer lowestValue = stockData.lv();
        Integer upperLimit = stockData.ul();
        Integer lowerLimit = stockData.ll();
        Integer openValue = stockData.ov();
        Integer standardValue = stockData.sv();
        Long transactionVolume = stockData.aq();
        Long transactionValue = stockData.aa();
        Long unixTimeMs = response.result().time();
        ZonedDateTime time = unixTimeMsToZonedDateTime(unixTimeMs);



        // rf(rise or fall)에 따라서 cv(change value)의 부호 변환
        changeValue = (Objects.equals(riseOrFall, "1") || Objects.equals(riseOrFall, "2")) ? changeValue : -changeValue;
        changeRate = (Objects.equals(riseOrFall, "1") || Objects.equals(riseOrFall, "2")) ? changeRate : -changeRate;


        return CurrentStockInfoDto.of(
                shortCode,
                stockName,
                marketState,
                marketType,
                nowValue,
                changeValue,
                changeRate,
                riseOrFall,
                highestValue,
                lowestValue,
                upperLimit,
                lowerLimit,
                openValue,
                standardValue,
                transactionVolume,
                transactionValue,
                time
        );
    }

    public ZonedDateTime unixTimeMsToZonedDateTime(Long unixTimeMs) {
        return Instant.ofEpochMilli(unixTimeMs)
                .atZone(ZoneId.of("Asia/Seoul"));
    }

}
