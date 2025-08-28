package arile.toy.stock_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    private LocalDateTime lastLoginAt;

    @Column
    private Double fee;

}
