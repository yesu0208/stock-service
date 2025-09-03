package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;

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

    @OneToMany(mappedBy = "chatroom")
    Set<GithubUserChatroomMapping> githubUserChatroomMappings = new HashSet<>();

    @Column(columnDefinition = "DATETIME")
    LocalDateTime createdAt;

    public Chatroom() {}

    public Chatroom(String title, LocalDateTime createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }

    public static Chatroom of(String title, LocalDateTime createdAt) {
        return new Chatroom(title, createdAt);
    }
}
