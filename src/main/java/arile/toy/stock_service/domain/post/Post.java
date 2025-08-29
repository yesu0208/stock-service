package arile.toy.stock_service.domain.post;

import arile.toy.stock_service.domain.GithubUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@ToString
@DynamicUpdate // JPA가 변경된 필드만 UPDATE SQL에 포함시킵니다.
@Table(name = "posts")
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Setter
    @Column
    private String title;

    @Setter
    @Column
    private String stockName;

    @Setter
    @Column(columnDefinition = "TEXT") // 긴 문자열
    private String body;

    @Setter
    @Column
    private Long repliesCount = 0L;

    @Setter
    @Column
    private Long likesCount = 0L;

    @Setter
    @Column
    private Long dislikesCount = 0L;

    @Setter
    @Column
    private LocalDateTime createdAt;

    @Setter
    @Column
    private LocalDateTime modifiedAt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "unchangeable_id")
    private GithubUserInfo user;

    protected Post() {}

    public Post(String title, String stockName, String body, Long repliesCount,
                Long likesCount, Long dislikesCount, GithubUserInfo user) {
        this.title = title;
        this.stockName = stockName;
        this.body = body;
        this.repliesCount = repliesCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.user = user;
    }

    public static Post of(String title, String stockName, String body, Long repliesCount,
                          Long likesCount, Long dislikesCount, GithubUserInfo user) {
        return new Post(title, stockName, body, repliesCount, likesCount, dislikesCount, user);
    }


    @PrePersist
    private void perPersist() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }


}
