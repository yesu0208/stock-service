package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.StaticStockInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static arile.toy.stock_service.domain.constant.MarketClass.KOSPI;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Repository] 종목 정보 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class StaticStockInfoRepositoryTest {

    @Autowired private StaticStockInfoRepository sut;

    @DisplayName("종목명으로 종목 정보를 반환한다.")
    @Test
    void givenStockName_whenSelectingStockInfo_thenReturnsStockInfo() {
        // Given
        String stockName = "SK하이닉스";

        // When
        Optional<StaticStockInfo> result = sut.findByStockName(stockName);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("stockName", "SK하이닉스")
                .hasFieldOrPropertyWithValue("shortCode", "000660")
                .hasFieldOrPropertyWithValue("marketClass", KOSPI);
    }

}