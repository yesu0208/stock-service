package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.interest.InterestGroup;
import arile.toy.stock_service.domain.interest.InterestStock;
import arile.toy.stock_service.dto.interestdto.InterestGroupDto;
import arile.toy.stock_service.dto.interestdto.InterestGroupWithCurrentInfoDto;
import arile.toy.stock_service.dto.interestdto.InterestStockDto;
import arile.toy.stock_service.dto.interestdto.InterestStockWithCurrentInfoDto;
import arile.toy.stock_service.exception.group.GroupNotFoundException;
import arile.toy.stock_service.repository.interest.InterestGroupRepository;
import arile.toy.stock_service.service.interest.InterestGroupService;
import arile.toy.stock_service.service.interest.InterestStockCurrentInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[Service] 관심 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class InterestGroupServiceTest {

    @InjectMocks private InterestGroupService sut;

    @Mock private InterestGroupRepository interestGroupRepository;
    @Mock private InterestStockCurrentInfoService interestStockCurrentInfoService;

    @DisplayName("unchangeableId가 주어지면, 유저의 관심 그룹 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenLoadingMyInterestGroupList_thenReturnsMyInterestGroupList() {
        // Given
        String unchangeableId = "123456";
        List<InterestGroup> interestGroups = List.of(
                InterestGroup.of("group_name1", "123456"),
                InterestGroup.of("group_name2", "123456")
        );
        given(interestGroupRepository.findByUnchangeableId(unchangeableId)).willReturn(interestGroups);

        // When
        List<InterestGroupDto> result = sut.getMyGroupList(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting("groupName")
                .containsExactly("group_name1", "group_name2");
        then(interestGroupRepository).should().findByUnchangeableId(unchangeableId);

    }


    @DisplayName("unchangeableId와 groupName이 주어지면, 유저의 관심 그룹을 반환한다.")
    @Test
    void givenUnchangeableIdAndGroupName_whenLoadingMyInterestGroup_thenReturnsMyInterestGroup() {
        // Given
        String unchangeableId = "123456";
        String groupName = "group_name";
        var interestGroup = InterestGroup.of("group_name", "123456");
        interestGroup.addInterestStock(InterestStock.of("삼성전자", 50000, 50, 50500, 2500000, 1));
        List<InterestStockDto> interestStockDtos = List.of(
                InterestStockDto.of(null, "삼성전자", 50000, 50, 50500, 2500000,
                1, null, null, null, null)
        );
        HashSet<InterestStockWithCurrentInfoDto> interestStockWithCurrentInfoDtos = new HashSet<>(List.of(
                InterestStockWithCurrentInfoDto.of(1L, "삼성전자", 50000, 50, 50500, 2500000, 1,
                        50500, 500, "1", 2500000, 10000, 10000 , "1", LocalDateTime.now(),
                        "test-name", LocalDateTime.now(), "test-name")
        ));
        given(interestGroupRepository.findByUnchangeableIdAndGroupName(unchangeableId, groupName))
                .willReturn(Optional.of(interestGroup));
        given(interestStockCurrentInfoService.getInterestStockSimpleCurrentInfo(interestStockDtos.getFirst(), unchangeableId))
                .willReturn(interestStockWithCurrentInfoDtos.stream().findFirst().orElse(null));

        // When
        InterestGroupWithCurrentInfoDto result = sut.getMyGroup(unchangeableId, groupName);

        // Then
        assertThat(result)
                .extracting("groupName")
                .isEqualTo("group_name");
        then(interestGroupRepository).should().findByUnchangeableIdAndGroupName(unchangeableId, groupName);
        then(interestStockCurrentInfoService).should().getInterestStockSimpleCurrentInfo(interestStockDtos.getFirst(), unchangeableId);

    }

    @DisplayName("unchangeableId와 groupName에 해당하는 그룹이 없으면, 예외를 던진다.")
    @Test
    void givenUnchangeableIdAndGroupName_whenLoadingNonexistingInterestGroup_thenThrowsException() {
        // Given
        String unchangeableId = "123456";
        String groupName = "group_name";
        given(interestGroupRepository.findByUnchangeableIdAndGroupName(unchangeableId, groupName))
                .willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> sut.getMyGroup(unchangeableId, groupName));

        // Then
        assertThat(thrown)
                .isInstanceOf(GroupNotFoundException.class)
                        .hasMessage("Group with groupName " + groupName + " not found in user unchangeableId : " + unchangeableId);
        then(interestGroupRepository).should().findByUnchangeableIdAndGroupName(unchangeableId, groupName);
        then(interestStockCurrentInfoService).shouldHaveNoInteractions();

    }

    @DisplayName("존재하지 않는 관심 그룹 정보가 주어지면, 관심 그룹을 생성한다.")
    @Test
    void givenNonExistentInterestGroup_whenUpserting_thenCreatesInterestGroup() {
        // Given
        var interestGroupDto = InterestGroupDto.of("group_name", "123456", Set.of());
        given(interestGroupRepository.save(any(InterestGroup.class)))
                .willReturn(null); // 무엇을 return하는지 test 관심사 아니니 그냥 null로 세팅

        // When
        sut.upsertInterestGroup(interestGroupDto);

        // Then
        then(interestGroupRepository).should().save(any(InterestGroup.class));

    }


    @DisplayName("존재하는 관심 그룹 정보가 주어지면, 관심 그룹을 수정한다.")
    @Test
    void givenExistentInterestGroup_whenUpserting_thenUpdatesInterestGroup() {
        // Given
        var interestGroupDto = InterestGroupDto.of("group_name", "123456", Set.of());
        var interestGroup = InterestGroup.of("group_name", "123456");
        given(interestGroupRepository.findByUnchangeableIdAndGroupName(interestGroupDto.unchangeableId(), interestGroupDto.groupName()))
                .willReturn(Optional.of(interestGroup));
        given(interestGroupRepository.save(any(InterestGroup.class)))
                .willReturn(null); // 무엇을 return하는지 test 관심사 아니니 그냥 null로 세팅

        // When
        sut.upsertInterestGroup(interestGroupDto);

        // Then
        then(interestGroupRepository).should().findByUnchangeableIdAndGroupName(interestGroupDto.unchangeableId(), interestGroupDto.groupName());
        then(interestGroupRepository).should().save(any(InterestGroup.class));

    }

    @DisplayName("unchangeableId와 groupName가 주어지면, 관심 그룹을 삭제한다.")
    @Test
    void givenUnchangeableIdAndGroupName_whenDeleting_thenDeletesInterestGroup() {
        // Given
        String unchangeableId = "123456";
        String groupName = "group_name";
        willDoNothing().given(interestGroupRepository).deleteByUnchangeableIdAndGroupName(unchangeableId, groupName);

        // When
        sut.deleteInterestGroup(unchangeableId, groupName);

        // Then
        then(interestGroupRepository).should().deleteByUnchangeableIdAndGroupName(unchangeableId, groupName);
    }

}
