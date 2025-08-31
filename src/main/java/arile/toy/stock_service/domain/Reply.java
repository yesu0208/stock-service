package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(name = "replies")
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String body;

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

    @Setter
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    protected Reply() {}

    public Reply(String body, GithubUserInfo user, Post post) {
        this.body = body;
        this.user = user;
        this.post = post;
    }

    public static Reply of(String body, GithubUserInfo user, Post post) {
        return new Reply(body, user, post);
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
        if (!(object instanceof Reply that)) return false;

        if (getReplyId() == null) {
            return Objects.equals(this.getBody(), that.getBody()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getModifiedAt(), that.getModifiedAt()) &&
                    Objects.equals(this.getUser(), that.getUser()) &&
                    Objects.equals(this.getPost(), that.getPost());
        }
        return Objects.equals(this.getReplyId(), that.getReplyId());
    }

    @Override
    public int hashCode() {
        if (getReplyId() == null) {
            return Objects.hash(getBody(), getCreatedAt(), getModifiedAt(), getUser(), getPost());
        }
        return Objects.hash(getReplyId());
    }
}
