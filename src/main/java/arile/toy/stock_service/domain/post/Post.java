package arile.toy.stock_service.domain.post;

import arile.toy.stock_service.domain.GithubUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Getter
@ToString
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_posts_unchangeable_id", columnList = "unchangeable_id"),
                @Index(name = "idx_posts_stock_name", columnList = "stock_name")
        }
)
@Entity
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Setter
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Setter
    @Column(name = "replies_count", nullable = false)
    private Long repliesCount = 0L;

    @Setter
    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @Setter
    @Column(name = "dislikes_count", nullable = false)
    private Long dislikesCount = 0L;

    @Setter
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "modified_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime modifiedAt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "unchangeable_id", nullable = false)
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
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.modifiedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }


    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
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
