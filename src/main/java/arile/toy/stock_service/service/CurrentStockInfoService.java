package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.naverstock.NaverStockResponse;
import arile.toy.stock_service.dto.InterestStockDto;
import arile.toy.stock_service.dto.InterestStockWithCurrentInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CurrentStockInfoService {

    private final RestClient restClient = RestClient.create();
    private final StockInfoService stockInfoService;

    public InterestStockWithCurrentInfoDto getCurrentStockInfo(InterestStockDto dto) {

        String shortCode = stockInfoService.loadShortCodeByStockName(dto.stockName());

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

        // rf(rise or fall)에 따라서 cv(change value)의 부호 변환
        String riseOrFall = response.result().areas().getFirst().datas().getFirst().rf();
        Integer changeValue = response.result().areas().getFirst().datas().getFirst().cv();
        changeValue = (Objects.equals(riseOrFall, "1") || Objects.equals(riseOrFall, "2")) ? changeValue : -changeValue;

        return InterestStockWithCurrentInfoDto.of(
                dto.id(),
                dto.stockName(),
                dto.buyingPrice(),
                dto.numOfStocks(),
                dto.breakEvenPrice(),
                dto.totalBuyingPrice(),
                dto.fieldOrder(),
                response.result().areas().getFirst().datas().getFirst().nv(),
                changeValue,
                riseOrFall,
                dto.createdAt(),
                dto.createdBy(),
                dto.createdAt(),
                dto.modifiedBy()
        );
    }
}
