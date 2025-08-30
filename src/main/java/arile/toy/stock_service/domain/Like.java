package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(name = "likes")
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "unchangeableId")
    private GithubUserInfo githubUserInfo;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    protected Like() {
    }

    public Like(GithubUserInfo githubUserInfo, Post post) {
        this.githubUserInfo = githubUserInfo;
        this.post = post;
    }

    public static Like of(GithubUserInfo githubUserInfo, Post post) {
        return new Like(githubUserInfo, post);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Like that)) return false;

        if (getLikeId() == null) {
            return Objects.equals(this.getGithubUserInfo(), that.getGithubUserInfo()) &&
                    Objects.equals(this.getPost(), that.getPost());
        }

        return Objects.equals(this.getLikeId(), that.getLikeId());
    }

    @Override
    public int hashCode() {
        if (getLikeId() == null) {
            return Objects.hash(getGithubUserInfo(), getPost());
        }
        return Objects.hash(getLikeId());
    }
}
