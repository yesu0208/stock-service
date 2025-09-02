//package arile.toy.stock_service.repository;
//
//import arile.toy.stock_service.domain.InterestStock;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.BDDAssertions.tuple;
//
//@DisplayName("[Repository] 관심 종목 쿼리 테스트")
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//class InterestStockRepositoryTest {
//
//    @Autowired InterestStockRepository sut;
//
//    @DisplayName("unchangeableId로 관심 종목 목록을 반환한다.")
//    @Test
//    void givenUnchangeableId_whenSelectingInterestGroupList_thenReturnsInterestGroupList() {
//        // Given
//        String unchangeableId = "123456";
//
//        // When
//        List<InterestStock> result = sut.findAllByInterestGroupUnchangeableId(unchangeableId);
//
//        // Then
//        assertThat(result)
//                .hasSize(2)
//                .extracting("stockName", "fieldOrder")
//                .contains(tuple("삼성전자", 1))
//                .contains(tuple("삼성전자", 2));
//    }
//
//}