package arile.toy.stock_service.service;

import arile.toy.stock_service.dto.interestdto.InterestGroupDto;
import arile.toy.stock_service.dto.interestdto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.exception.group.GroupNotFoundException;
import arile.toy.stock_service.repository.interest.InterestGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class InterestGroupService {

    private final InterestGroupRepository interestGroupRepository;
    private final InterestStockCurrentInfoService interestStockCurrentInfoService;

    @Transactional(readOnly = true)
    public List<InterestGroupDto> loadMyGroups(String unchangeableId) {
        return interestGroupRepository.findByUnchangeableId(unchangeableId)
                .stream()
                .map(InterestGroupDto::fromEntity)
                .toList();
    }


    @Transactional(readOnly = true)
    public InterestGroupWithCurrentInfoDto loadMyGroup(String unchangeableId, String groupName) {
        var interestGroupDto = interestGroupRepository.findByUnchangeableIdAndGroupName(unchangeableId, groupName)
                .map(InterestGroupDto::fromEntity)
                // Optional
                .orElseThrow(() -> new GroupNotFoundException(unchangeableId, groupName)); // optional이므로

        var interestStockWithCurrentInfoDtos = interestGroupDto.interestStockDtos()
                .stream()
                .map(interestStockDto
                        -> interestStockCurrentInfoService.getInterestStockSimpleCurrentInfo(interestStockDto, unchangeableId))
                .collect(Collectors.toUnmodifiableSet());

        InterestGroupWithCurrentInfoDto response = new InterestGroupWithCurrentInfoDto(
                interestGroupDto.interestGroupId(),
                interestGroupDto.groupName(),
                interestGroupDto.unchangeableId(),
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
        interestGroupRepository.findByUnchangeableIdAndGroupName(dto.unchangeableId(), dto.groupName())
                .ifPresentOrElse( // Optional
                        entity -> interestGroupRepository.save(dto.updateEntity(entity)),
                        () -> interestGroupRepository.save(dto.createEntity())
                );

    }

    public void deleteInterestGroup(String unchangeableId, String groupName) {
        interestGroupRepository.deleteByUnchangeableIdAndGroupName(unchangeableId, groupName);
    }
}
