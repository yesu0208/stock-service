package arile.toy.stock_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Table(name = "github_user_chatroom_mappings")
@Entity
public class GithubUserChatroomMapping {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "github_user_chatroom_mapping_id")
    @Id
    Long githubUserChatroomMappingId;

    @JoinColumn(name = "unchangeableId")
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
}
