package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.interest.InterestStock;
import arile.toy.stock_service.dto.GithubUserCurrentAccountDto;
import arile.toy.stock_service.dto.GithubUserInfoDto;
import arile.toy.stock_service.dto.InterestStockDto;
import arile.toy.stock_service.dto.InterestStockWithCurrentInfoDto;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.InterestStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("[Service] 유저 정보 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class GithubUserInfoServiceTest {

    @InjectMocks private GithubUserInfoService sut;

    @Mock private GithubUserInfoRepository githubUserInfoRepository;
    @Mock private InterestStockCurrentInfoService interestStockCurrentInfoService;
    @Mock private InterestStockRepository interestStockRepository;


    @DisplayName("unchangeableId가 주어지면 해당 사용자 정보를 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingGithubUserInfo_thenReturnsGithubUserInfo() {
        // Given
        String unchangeableId = "123456";
        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));

        // When
        GithubUserInfoDto result = sut.loadGithubUserInfo(unchangeableId);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("id", "test-id")
                .hasFieldOrPropertyWithValue("name", "test-name")
                .hasFieldOrPropertyWithValue("email", "test@email.com")
                .hasFieldOrPropertyWithValue("fee", 0.1);
        then(githubUserInfoRepository).should().findById(unchangeableId);
    }


    @DisplayName("unchangeableId에 해당하는 사용자 정보가 없으면, 예외를 던진다.")
    @Test
    void givenUnchangeableId_whenLoadingNonexistentGithubUserInfo_thenThrowsException() {
        // Given
        String unchangeableId = "123456";
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> sut.loadGithubUserInfo(unchangeableId));

        // Then
        assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with unchangeableId " + unchangeableId + " not found");
        then(githubUserInfoRepository).should().findById(unchangeableId);
    }


    @DisplayName("unchangeableId와 fee가 주어지면 해당 사용자의 fee를 수정한다.")
    @Test
    void givenUnchangeableIdAndFeeRate_whenUpdating_thenUpdatesUserFee() {
        // Given
        String unchangeableId = "123456";
        Double feeRate = 20.0;
        Double fee = feeRate/100;
        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
        githubUserInfo.setFee(fee);
        given(githubUserInfoRepository.save(githubUserInfo)).willReturn(githubUserInfo);

        // When
        sut.updateGithubUserFee(unchangeableId, fee);

        // Then
        then(githubUserInfoRepository).should().findById(unchangeableId);
        then(githubUserInfoRepository).should().save(githubUserInfo);
    }


    @DisplayName("unchangeableId에 해당하는 사용자 정보가 없으면, fee를 수정하지 않고 예외를 던진다.")
    @Test
    void givenUnchangeableIdAndFee_whenUpdatingNonexistentGithubUserInfo_thenThrowsException() {
        // Given
        String unchangeableId = "123456";
        Double fee = 0.2;
        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> sut.updateGithubUserFee(unchangeableId, fee));

        // Then
        assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with unchangeableId " + unchangeableId + " not found");
        then(githubUserInfoRepository).should().findById(unchangeableId);
        then(githubUserInfoRepository).should(never()).save(any(GithubUserInfo.class));
    }

    @DisplayName("unchangeableId가 주어지면, 유저의 현재 (관심 그룹의) 재무 정보를 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingGithubUserCurrentAccount_thenReturnsGithubUserCurrentAccount() {
        // Given
        String unchangeableId = "123456";
        List<InterestStock> interestStocks = List.of(
                InterestStock.of("삼성전자", 50000, 50, 50500, 2500000, 1)
        );
        List<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos = List.of(
                InterestStockWithCurrentInfoDto.of(1L, "삼성전자", 50000, 50, 50500, 2500000, 1, 50500, 500, "0.1",
                        2525000, 0, 0, "10", LocalDateTime.now(), "test-name", LocalDateTime.now(), "test-name")
        );
        given(interestStockRepository.findAllByInterestGroupUnchangeableId(unchangeableId)).willReturn(interestStocks);
        given(interestStockCurrentInfoService.getInterestStockSimpleCurrentInfo(
                InterestStockDto.fromEntity(interestStocks.getFirst()), unchangeableId)).willReturn(interestStockWithCurrentInfoDtos.getFirst());

        // When
        GithubUserCurrentAccountDto result = sut.loadGithubUserCurrentAccount(unchangeableId);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("totalBuyingPrice", 2500000)
                        .hasFieldOrPropertyWithValue("totalValuation", 2525000);
        then(interestStockRepository).should().findAllByInterestGroupUnchangeableId(unchangeableId);
        then(interestStockCurrentInfoService).should().getInterestStockSimpleCurrentInfo(
                InterestStockDto.fromEntity(interestStocks.getFirst()), unchangeableId);
    }
}