package arile.toy.stock_service.service;

import arile.toy.stock_service.dto.GithubUserCurrentAccountDto;
import arile.toy.stock_service.dto.GithubUserInfoDto;
import arile.toy.stock_service.dto.interestdto.InterestStockDto;
import arile.toy.stock_service.dto.interestdto.InterestStockWithCurrentInfoDto;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.InterestStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GithubUserInfoService {

    private final GithubUserInfoRepository githubUserInfoRepository;
    private final InterestStockCurrentInfoService interestStockCurrentInfoService;
    private final InterestStockRepository interestStockRepository;

    public GithubUserInfoDto loadGithubUserInfo(String unchangeableId) {

        return githubUserInfoRepository.findById(unchangeableId)
                .map(GithubUserInfoDto::fromEntity)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));
    }

    public void updateGithubUserFee(String unchangeableId, Double feeRate) {
        githubUserInfoRepository.findById(unchangeableId)
                .ifPresentOrElse(
                        entity -> {
                            entity.setFee(feeRate/100);
                            githubUserInfoRepository.save(entity);
                        },
                        () -> { throw new UserNotFoundException(unchangeableId); }
                );
    }

    public GithubUserCurrentAccountDto loadGithubUserCurrentAccount(String unchangeableId) {
        List<InterestStockDto> interestStockDtos
                = interestStockRepository.findAllByInterestGroupUnchangeableId(unchangeableId)
                .stream()
                .map(InterestStockDto::fromEntity)
                .toList();

        List<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos
                = interestStockDtos.stream()
                        .map(each
                                -> interestStockCurrentInfoService.getInterestStockSimpleCurrentInfo(each, unchangeableId)
                        )
                        .toList();

        Integer totalBuyingPrice = interestStockWithCurrentInfoDtos.stream()
                .filter(dto -> dto.buyingPrice() != null && dto.numOfStocks() != null) // null 체크
                .mapToInt(dto -> dto.totalBuyingPrice() != null ? dto.totalBuyingPrice() : 0)
                .sum();

        Integer totalValuation = interestStockWithCurrentInfoDtos.stream()
                .filter(dto -> dto.buyingPrice() != null && dto.numOfStocks() != null) // null 체크
                .mapToInt(dto -> dto.valuation() != null ? dto.valuation() : 0)
                .sum();

        Integer totalUnrealizedPL = interestStockWithCurrentInfoDtos.stream()
                .filter(dto -> dto.buyingPrice() != null && dto.numOfStocks() != null) // null 체크
                .mapToInt(dto -> dto.unrealizedPL() != null ? dto.unrealizedPL() : 0)
                .sum();

        Integer totalRealizedPL = interestStockWithCurrentInfoDtos.stream()
                .filter(dto -> dto.buyingPrice() != null && dto.numOfStocks() != null) // null 체크
                .mapToInt(dto -> dto.realizedPL() != null ? dto.realizedPL() : 0)
                .sum();

        Double rateOfReturn = (totalBuyingPrice == 0) ? 0.0 :
                                ((double) totalRealizedPL / ((double) totalBuyingPrice)) * 100;
        String rateOfReturnString = String.format("%.2f%%", rateOfReturn);

        return GithubUserCurrentAccountDto.of(unchangeableId, totalBuyingPrice, totalValuation, totalUnrealizedPL, totalRealizedPL, rateOfReturnString);

    }

}
