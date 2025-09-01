package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.naverstock.NaverStockResponse;
import arile.toy.stock_service.dto.InterestStockDto;
import arile.toy.stock_service.dto.InterestStockWithCurrentInfoDto;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class InterestStockCurrentInfoService {

    private final RestClient restClient = RestClient.create();
    private final StaticStockInfoService stockInfoService;
    private final GithubUserInfoRepository githubUserInfoRepository;

    public InterestStockWithCurrentInfoDto getInterestStockSimpleCurrentInfo(InterestStockDto dto, String unchangeableId) {

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
        Double changeRate = response.result().areas().getFirst().datas().getFirst().cr();
        Integer nowValue = response.result().areas().getFirst().datas().getFirst().nv();

        changeValue = (Objects.equals(riseOrFall, "1") || Objects.equals(riseOrFall, "2")) ? changeValue : -changeValue;
        changeRate = (Objects.equals(riseOrFall, "1") || Objects.equals(riseOrFall, "2")) ? changeRate : -changeRate;
        String changeRateString = String.format("(%.2f%%)", changeRate);

        Integer valuation = null;
        if (dto.numOfStocks() != null) {
            valuation = nowValue * dto.numOfStocks();
        }

        Integer unrealizedPL = null;
        Integer realizedPL = null;
        Double rateOfReturn = null;
        String rateOfReturnString = null;
        Double fee = githubUserInfoRepository.findById(unchangeableId)
                .map(GithubUserInfo::getFee)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + unchangeableId));

        if (dto.buyingPrice() != null && dto.numOfStocks() != null) {
            unrealizedPL = (nowValue - dto.buyingPrice()) * dto.numOfStocks();
            realizedPL = unrealizedPL - (int) Math.round(dto.buyingPrice() * dto.numOfStocks() * fee);
            rateOfReturn = ((double) realizedPL / ((double) dto.buyingPrice() * dto.numOfStocks())) * 100;
            rateOfReturnString = String.format("(%.2f%%)", rateOfReturn);
        }

        return InterestStockWithCurrentInfoDto.of(
                dto.interestStockId(),
                dto.stockName(),
                dto.buyingPrice(),
                dto.numOfStocks(),
                dto.breakEvenPrice(),
                dto.totalBuyingPrice(),
                dto.fieldOrder(),
                nowValue,
                changeValue,
                changeRateString,
                valuation,
                unrealizedPL,
                realizedPL,
                rateOfReturnString,
                dto.createdAt(),
                dto.createdBy(),
                dto.createdAt(),
                dto.modifiedBy()
        );
    }
}
