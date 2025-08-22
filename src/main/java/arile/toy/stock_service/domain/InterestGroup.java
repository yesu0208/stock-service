package arile.toy.stock_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(name = "interest_groups")
@Entity
public class InterestGroup extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String groupName;

    @Setter
    @Column(nullable = false)
    private String userId;

    // final은 합당 : 여기에 새로운 LinkedHashSet을 넣는 것이 아닌 단순 add, delete만 하므로
    @ToString.Exclude
    @OrderBy("fieldOrder ASC")
    @OneToMany(mappedBy = "interestGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<InterestStock> interestStocks = new LinkedHashSet<>(); // 순서가 있는 set : 관심종목은 순서가 있다.

    protected InterestGroup() {
    }

    public InterestGroup(String groupName, String userId) {
        this.groupName = groupName;
        this.userId = userId;
    }

    public static InterestGroup of(String schemaName, String userId) {
        return new InterestGroup(schemaName, userId);
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
}

