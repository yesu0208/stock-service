package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import arile.toy.stock_service.dto.security.GithubUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Table(name = "chatrooms")
@Entity
public class Chatroom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Id
    Long chatroomId;

    @Column
    String title;

    @Column
    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<GithubUserChatroomMapping> githubUserChatroomMappings = new HashSet<>();

    @Column(columnDefinition = "DATETIME")
    LocalDateTime createdAt;

    @Column
    String stockName;

    @Column
    String createdBy;

    @Column
    String unchangeableId;

    @Setter
    @Transient
    Boolean hasNewMessage;

    public Chatroom() {}

    public Chatroom(String title, LocalDateTime createdAt, String stockName, String createdBy, String unchangeableId) {
        this.title = title;
        this.createdAt = createdAt;
        this.stockName = stockName;
        this.createdBy = createdBy;
        this.unchangeableId = unchangeableId;
    }

    public static Chatroom of(String title, LocalDateTime createdAt, String stockName, String createdBy, String unchangeableId) {
        return new Chatroom(title, createdAt, stockName, createdBy, unchangeableId);
    }

    public GithubUserChatroomMapping addGithubUserInfo(GithubUserInfo githubUserInfo) {

        GithubUserChatroomMapping githubUserChatroomMapping =
                GithubUserChatroomMapping.of(githubUserInfo, this);

        this.githubUserChatroomMappings.add(githubUserChatroomMapping);

        return  githubUserChatroomMapping;
    }
}
