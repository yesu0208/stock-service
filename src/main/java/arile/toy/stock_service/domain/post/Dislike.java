package arile.toy.stock_service.domain.post;

import arile.toy.stock_service.domain.GithubUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(
        name = "dislikes",
        indexes = {
                @Index(name = "idx_dislikes_unchangeable_id", columnList = "unchangeable_id"),
                @Index(name = "idx_dislikes_post_id", columnList = "post_id")
        }
)
@Entity
public class Dislike {

    @Id
    @Column(name = "dislike_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislikeId;

    @ManyToOne
    @JoinColumn(name = "unchangeable_id", nullable = false)
    private GithubUserInfo githubUserInfo;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    protected Dislike() {
    }


    public Dislike(GithubUserInfo githubUserInfo, Post post) {
        this.githubUserInfo = githubUserInfo;
        this.post = post;
    }


    public static Dislike of(GithubUserInfo githubUserInfo, Post post) {
        return new Dislike(githubUserInfo, post);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Dislike that)) return false;

        if (getDislikeId() == null) {
            return Objects.equals(this.getGithubUserInfo(), that.getGithubUserInfo()) &&
                    Objects.equals(this.getPost(), that.getPost());
        }

        return Objects.equals(this.getDislikeId(), that.getDislikeId());
    }


    @Override
    public int hashCode() {
        if (getDislikeId() == null) {
            return Objects.hash(getGithubUserInfo(), getPost());
        }
        return Objects.hash(getDislikeId());
    }
}
