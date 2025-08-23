package arile.toy.stock_service.service;

import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.dto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.dto.InterestStockWithCurrentInfoDto;
import arile.toy.stock_service.repository.InterestGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class InterestGroupService {

    private final InterestGroupRepository interestGroupRepository;
    private final CurrentStockInfoService currentStockInfoService;

    @Transactional(readOnly = true)
    public List<InterestGroupDto> loadMyGroups(String userId) {
        return interestGroupRepository.findByUserId(userId)
                .stream()
                .map(InterestGroupDto::fromEntity)
                .toList();
    }


    @Transactional(readOnly = true)
    public InterestGroupWithCurrentInfoDto loadMyGroup(String userId, String groupName) {
        var interestGroupDto = interestGroupRepository.findByUserIdAndGroupName(userId, groupName)
                .map(InterestGroupDto::fromEntity)
                // Optional
                .orElseThrow(() -> new EntityNotFoundException("관심 그룹이 없습니다 - userId: "
                        + userId
                        + ", groupName: "
                        +groupName)); // optional이므로

        var interestStockWithCurrentInfoDtos = interestGroupDto.interestStocks()
                .stream()
                .map(currentStockInfoService::getCurrentStockInfo)
                .collect(Collectors.toUnmodifiableSet());

        InterestGroupWithCurrentInfoDto response = new InterestGroupWithCurrentInfoDto(
                interestGroupDto.id(),
                interestGroupDto.groupName(),
                interestGroupDto.userId(),
                new HashSet<>(),
                interestGroupDto.createdAt(),
                interestGroupDto.createdBy(),
                interestGroupDto.modifiedAt(),
                interestGroupDto.modifiedBy()
        );

        response.addInterestStockWithCurrentInfoDtos(interestStockWithCurrentInfoDtos);

        return response;
    }

    public void upsertInterestGroup(InterestGroupDto dto){
        interestGroupRepository.findByUserIdAndGroupName(dto.userId(), dto.groupName())
                .ifPresentOrElse( // Optional
                        entity -> interestGroupRepository.save(dto.updateEntity(entity)),
                        () -> interestGroupRepository.save(dto.createEntity())
                );

    }

    public void deleteInterestGroup(String userId, String schemaName) {
        interestGroupRepository.deleteByUserIdAndGroupName(userId, schemaName);
    }
}
