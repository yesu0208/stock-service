package arile.toy.stock_service.domain.chat;

import arile.toy.stock_service.domain.GithubUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(
        name = "github_user_chatroom_mappings",
        indexes = {
                @Index(name = "idx_gucm_unchangeable_id", columnList = "unchangeable_id"),
                @Index(name = "idx_gucm_chatroom_id", columnList = "chatroom_id")
        }
)
@Entity
public class GithubUserChatroomMapping {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "github_user_chatroom_mapping_id")
    @Id
    private Long githubUserChatroomMappingId;

    @JoinColumn(name = "unchangeable_id", nullable = false)
    @ManyToOne(optional = false)
    private GithubUserInfo githubUserInfo;

    @JoinColumn(name = "chatroom_id", nullable = false)
    @ManyToOne(optional = false)
    private Chatroom chatroom;

    @Setter
    @Column(name = "last_checked_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime lastCheckedAt;


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
