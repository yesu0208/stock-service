package arile.toy.stock_service.config;

import arile.toy.stock_service.domain.naverstock.NaverStockResponse;
import arile.toy.stock_service.service.StockInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {
    private static final RestClient restClient = RestClient.create();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    @Autowired StockInfoService stockInfoService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

     //       getCurrentStockInfo("삼성전자보통주");

            }
        };
    }

    public void getCurrentStockInfo(String stockName) throws JsonProcessingException {

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

        logger.info(response.toString());
    }

}
