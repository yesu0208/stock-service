package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    @Column
    private LocalDateTime createdAt;

    @Setter
    @Column
    private LocalDateTime modifiedAt;

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
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

}
