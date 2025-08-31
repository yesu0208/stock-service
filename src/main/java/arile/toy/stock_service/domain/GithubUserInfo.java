package arile.toy.stock_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Objects;

@Setter
@Getter
@ToString
@Table(name = "github_user_information")
@Entity
public class GithubUserInfo {

    @Id
    private String unchangeableId;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private ZonedDateTime lastLoginAt;

    @Column
    private Double fee;

    public GithubUserInfo() {
    }

    public GithubUserInfo(String unchangeableId, String id, String name, String email, Double fee) {
        this.unchangeableId = unchangeableId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.fee = fee;
    }

    public static GithubUserInfo of(String unchangeableId, String id, String name, String email, Double fee) {
        return new GithubUserInfo(unchangeableId, id, name, email, fee);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GithubUserInfo that)) return false;

        if (getUnchangeableId() == null) {
            return Objects.equals(this.getId(), that.getId()) &&
                    Objects.equals(this.getName(), that.getName()) &&
                    Objects.equals(this.getEmail(), that.getEmail()) &&
                    Objects.equals(this.getLastLoginAt(), that.getLastLoginAt()) &&
                    Objects.equals(this.getFee(), that.getFee());
        }

        return Objects.equals(this.getUnchangeableId(), that.getUnchangeableId());
    }

    @Override
    public int hashCode() {
        if (getUnchangeableId() == null) {
            return Objects.hash(getId(), getName(), getEmail(), getLastLoginAt(), getFee());
        }
        return Objects.hash(getUnchangeableId());
    }
}
