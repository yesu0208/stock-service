package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.dto.GithubUserInfoDto;
import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GithubUserInfoService {

    private final GithubUserInfoRepository githubUserInfoRepository;

    public GithubUserInfoDto loadGithubUserInfo(String unchangeableId) {

        return githubUserInfoRepository.findById(unchangeableId)
                .map(GithubUserInfoDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다 - unchangeableId: " + unchangeableId));
    }

    public void updateGithubUserFee(String unchangeableId, GithubUserInfoDto dto) {
        GithubUserInfo entity = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다 - unchangeableId: " + unchangeableId));

        githubUserInfoRepository.save(dto.updateEntityFee(entity, dto.fee()));
    }

}
