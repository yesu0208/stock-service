package arile.toy.stock_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Table(name = "github_user_chatroom_mappings")
@Entity
public class GithubUserChatroomMapping {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "github_user_chatroom_mapping_id")
    @Id
    Long githubUserChatroomMappingId;

    @JoinColumn(name = "unchangeable_id")
    @ManyToOne
    GithubUserInfo githubUserInfo;

    @JoinColumn(name = "chatroom_id")
    @ManyToOne
    Chatroom chatroom;

    @Setter
    @Column(columnDefinition = "DATETIME")
    LocalDateTime lastCheckedAt;


    public GithubUserChatroomMapping() {}

    public GithubUserChatroomMapping(GithubUserInfo githubUserInfo, Chatroom chatroom) {
        this.githubUserInfo = githubUserInfo;
        this.chatroom = chatroom;
    }

    public static GithubUserChatroomMapping of(GithubUserInfo githubUserInfo, Chatroom chatroom) {
        return new GithubUserChatroomMapping(githubUserInfo, chatroom);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GithubUserChatroomMapping that)) return false;

        if (getGithubUserChatroomMappingId() == null) {
            return Objects.equals(this.getGithubUserInfo(), that.getGithubUserInfo()) &&
                    Objects.equals(this.getChatroom(), that.getChatroom()) &&
                    Objects.equals(this.getLastCheckedAt(), that.getLastCheckedAt());
        }

        return Objects.equals(this.getGithubUserChatroomMappingId(), that.getGithubUserChatroomMappingId());
    }

    @Override
    public int hashCode() {
        if (getGithubUserChatroomMappingId() == null) {
            return Objects.hash(getGithubUserInfo(), getChatroom(), getLastCheckedAt());
        }
        return Objects.hash(getGithubUserChatroomMappingId());
    }
}
