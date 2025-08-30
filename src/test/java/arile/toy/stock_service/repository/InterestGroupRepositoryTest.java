package arile.toy.stock_service.repository;

import arile.toy.stock_service.config.JpaConfig;
import arile.toy.stock_service.domain.InterestGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;

@DisplayName("[Repository] 관심 그룹 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class InterestGroupRepositoryTest {

    @Autowired InterestGroupRepository sut;

    @DisplayName("unchangeableId로 관심 그룹 목록을 반환한다.")
    @Test
    void givenUnchangeableId_whenSelectingInterestGroupList_thenReturnsInterestGroupList() {
        // Given
        String unchangeableId = "123456";

        // When
        List<InterestGroup> result = sut.findByUnchangeableId(unchangeableId);

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting("unchangeableId", "groupName")
                .containsOnly(tuple("123456", "group_name"));
    }


    @DisplayName("unchangeableId와 groupName으로 관심 그룹을 반환한다.")
    @Test
    void givenUnchangeableIdAndGroupName_whenSelectingInterestGroup_thenReturnsInterestGroup() {
        // Given
        String unchangeableId = "123456";
        String groupName = "group_name";

        // When
        Optional<InterestGroup> result = sut.findByUnchangeableIdAndGroupName(unchangeableId, groupName);

        // Then
        assertThat(result)
                .get()
                .hasFieldOrPropertyWithValue("unchangeableId", unchangeableId)
                .hasFieldOrPropertyWithValue("groupName", groupName);
    }


    @DisplayName("unchangeableId와 groupName으로 관심 그룹을 삭제한다.")
    @Test
    void givenUnchangeableIdAndGroupName_whenDeletingInterestGroup_thenDeletesInterestGroup() {
        // Given
        String unchangeableId = "123456";
        String groupName = "group_name";

        // When
        sut.deleteByUnchangeableIdAndGroupName(unchangeableId, groupName);

        // Then
        assertThat(sut.findByUnchangeableIdAndGroupName(unchangeableId, groupName)).isEmpty();
    }
}