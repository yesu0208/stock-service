package arile.toy.stock_service.domain.interest;

import arile.toy.stock_service.domain.auditing.AuditingFields;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(name = "interest_groups")
@Entity
public class InterestGroup extends AuditingFields {

    @Id
    @Column(name = "interest_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestGroupId;

    @Setter
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Setter
    @Column(name = "unchangeable_id", nullable = false)
    private String unchangeableId;

    @ToString.Exclude
    @OrderBy("fieldOrder ASC")
    @OneToMany(mappedBy = "interestGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<InterestStock> interestStocks = new LinkedHashSet<>();


    protected InterestGroup() {
    }


    public InterestGroup(String groupName, String unchangeableId) {
        this.groupName = groupName;
        this.unchangeableId = unchangeableId;
    }


    public static InterestGroup of(String groupName, String unchangeableId) {
        return new InterestGroup(groupName, unchangeableId);
    }


    public void addInterestStocks(Collection<InterestStock> interestStocks) {
        interestStocks.forEach(this::addInterestStock);
    }


    public void addInterestStock(InterestStock interestStock) {
        interestStocks.add(interestStock);
        interestStock.setInterestGroup(this);
    }


    public void clearInterestStocks() {
        interestStocks.clear();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InterestGroup that)) return false;

        if (getInterestGroupId() == null) {
            return Objects.equals(this.getGroupName(), that.getGroupName()) &&
                    Objects.equals(this.getUnchangeableId(), that.getUnchangeableId());
        }
        return Objects.equals(this.getInterestGroupId(), that.getInterestGroupId());
    }


    @Override
    public int hashCode() {
        if (getInterestGroupId() == null) {
            return Objects.hash(getGroupName(), getUnchangeableId());
        }
        return Objects.hash(getInterestGroupId());
    }
}

