package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Table(name = "dislikes")
@Entity
public class Dislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislikeId;

    @ManyToOne
    @JoinColumn(name = "unchangeableId")
    private GithubUserInfo githubUserInfo;

    @ManyToOne
    @JoinColumn(name = "postId")
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
}
