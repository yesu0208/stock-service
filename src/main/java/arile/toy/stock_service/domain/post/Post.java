package arile.toy.stock_service.domain.post;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.StockInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

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
    @Column(columnDefinition = "DATETIME")
    private ZonedDateTime createdAt;

    @Setter
    @Column(columnDefinition = "DATETIME")
    private ZonedDateTime modifiedAt;

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
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.modifiedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Post that)) return false;
        if (getPostId() == null) {
            return Objects.equals(this.getTitle(), that.getTitle()) &&
                    Objects.equals(this.getStockName(), that.getStockName()) &&
                    Objects.equals(this.getBody(), that.getBody()) &&
                    Objects.equals(this.getRepliesCount(), that.getRepliesCount()) &&
                    Objects.equals(this.getLikesCount(), that.getLikesCount()) &&
                    Objects.equals(this.getDislikesCount(), that.getDislikesCount()) &&
                    Objects.equals(this.getUser(), that.getUser()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getModifiedAt(), that.getModifiedAt());
        }
        return Objects.equals(this.getPostId(), that.getPostId());
    }

    @Override
    public int hashCode() {
        if (getPostId() == null) {
            return Objects.hash(getTitle(), getStockName(), getBody(), getRepliesCount(), getLikesCount(), getDislikesCount(), getCreatedAt(), getModifiedAt(), getUser());
        }
        return Objects.hash(getPostId());
    }




// Test용

    public Post(Long postId, String title, String stockName, String body, Long repliesCount,
                Long likesCount, Long dislikesCount, GithubUserInfo user) {
        this.postId = postId;
        this.title = title;
        this.stockName = stockName;
        this.body = body;
        this.repliesCount = repliesCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.user = user;
    }

    public static Post of(Long postId, String title, String stockName, String body, Long repliesCount,
                          Long likesCount, Long dislikesCount, GithubUserInfo user) {
        return new Post(postId, title, stockName, body, repliesCount, likesCount, dislikesCount, user);
    }

}
