package arile.toy.stock_service.service;

import arile.toy.stock_service.dto.InterestGroupDto;
import arile.toy.stock_service.repository.InterestGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class InterestGroupService {

    private final InterestGroupRepository interestGroupRepository;

    @Transactional(readOnly = true)
    public List<InterestGroupDto> loadMyGroups(String userId) {
        return interestGroupRepository.findByUserId(userId)
                .stream()
                .map(InterestGroupDto::fromEntity)
                .toList();
    }


    @Transactional(readOnly = true)
    public InterestGroupDto loadMyGroup(String userId, String groupName) {
        return interestGroupRepository.findByUserIdAndGroupName(userId, groupName)
                .map(InterestGroupDto::fromEntity)
                // Optional
                .orElseThrow(() -> new EntityNotFoundException("관심 그룹이 없습니다 - userId: "
                        + userId
                        + ", groupName: "
                        +groupName)); // optional이므로
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
