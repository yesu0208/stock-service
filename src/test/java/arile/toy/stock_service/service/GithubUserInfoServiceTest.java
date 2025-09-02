//package arile.toy.stock_service.service;
//
//import arile.toy.stock_service.domain.GithubUserInfo;
//import arile.toy.stock_service.dto.GithubUserInfoDto;
//import arile.toy.stock_service.exception.user.UserNotFoundException;
//import arile.toy.stock_service.repository.GithubUserInfoRepository;
//import arile.toy.stock_service.repository.InterestStockRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchThrowable;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
//@DisplayName("[Service] 유저 정보 서비스 테스트")
//@ExtendWith(MockitoExtension.class)
//class GithubUserInfoServiceTest {
//
//    @InjectMocks private GithubUserInfoService sut;
//
//    @Mock private GithubUserInfoRepository githubUserInfoRepository;
//    @Mock private InterestStockCurrentInfoService interestStockCurrentInfoService;
//    @Mock private InterestStockRepository interestStockRepository;
//
//
//    @DisplayName("unchangeableId가 주어지면 해당 사용자 정보를 반환한다.")
//    @Test
//    void givenUnchangeableId_whenLoadingGithubUserInfo_thenReturnsGithubUserInfo() {
//        // Given
//        String unchangeableId = "123456";
//        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
//        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
//
//        // When
//        GithubUserInfoDto result = sut.loadGithubUserInfo(unchangeableId);
//
//        // Then
//        assertThat(result)
//                .hasFieldOrPropertyWithValue("unchangeableId", unchangeableId)
//                .hasFieldOrPropertyWithValue("id", "test-id")
//                .hasFieldOrPropertyWithValue("name", "test-name")
//                .hasFieldOrPropertyWithValue("email", "test@email.com")
//                .hasFieldOrPropertyWithValue("fee", 0.1);
//        then(githubUserInfoRepository).should().findById(unchangeableId);
//    }
//
//
//    @DisplayName("unchangeableId에 해당하는 사용자 정보가 없으면, 예외를 던진다.")
//    @Test
//    void givenUnchangeableId_whenLoadingNonexistentGithubUserInfo_thenThrowsException() {
//        // Given
//        String unchangeableId = "123456";
//        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.empty());
//
//        // When
//        Throwable thrown = catchThrowable(() -> sut.loadGithubUserInfo(unchangeableId));
//
//        // Then
//        assertThat(thrown)
//                .isInstanceOf(UserNotFoundException.class)
//                .hasMessage("User with unchangeableId " + unchangeableId + " not found");
//        then(githubUserInfoRepository).should().findById(unchangeableId);
//    }
//
//
//    @DisplayName("unchangeableId와 fee가 주어지면 해당 사용자의 fee를 수정한다.")
//    @Test
//    void givenUnchangeableIdAndFee_whenUpdating_thenUpdatesUserFee() {
//        // Given
//        String unchangeableId = "123456";
//        Double fee = 0.2;
//        var githubUserInfoDto = GithubUserInfoDto.of(fee);
//        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
//        GithubUserInfo updatedEntity = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.2);
//        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.of(githubUserInfo));
//        given(githubUserInfoRepository.save(githubUserInfoDto.updateEntityFee(githubUserInfo, fee))).willReturn(updatedEntity);
//
//        // When
//        sut.updateGithubUserFee(unchangeableId, githubUserInfoDto);
//
//        // Then
//        then(githubUserInfoRepository).should().findById(unchangeableId);
//        then(githubUserInfoRepository).should().save(githubUserInfoDto.updateEntityFee(githubUserInfo, fee));
//    }
//
//
//    @DisplayName("unchangeableId에 해당하는 사용자 정보가 없으면, fee를 수정하지 않고 예외를 던진다.")
//    @Test
//    void givenUnchangeableIdAndFee_whenUpdatingNonexistentGithubUserInfo_thenThrowsException() {
//        // Given
//        String unchangeableId = "123456";
//        Double fee = 0.2;
//        var githubUserInfoDto = GithubUserInfoDto.of(fee);
//        var githubUserInfo = GithubUserInfo.of(unchangeableId, "test-id", "test-name", "test@email.com", 0.1);
//        given(githubUserInfoRepository.findById(unchangeableId)).willReturn(Optional.empty());
//
//        // When
//        Throwable thrown = catchThrowable(() -> sut.updateGithubUserFee(unchangeableId, githubUserInfoDto));
//
//        // Then
//        assertThat(thrown)
//                .isInstanceOf(UserNotFoundException.class)
//                .hasMessage("User with unchangeableId " + unchangeableId + " not found");
//        then(githubUserInfoRepository).should().findById(unchangeableId);
//        then(githubUserInfoRepository).should(times(0)).save(githubUserInfoDto.updateEntityFee(githubUserInfo, fee));
//    }
//}