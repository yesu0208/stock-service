package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Table(name = "messages")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long messageId;

    @Column(columnDefinition = "TEXT")
    String text;

    @ManyToOne
    @JoinColumn(name = "unchangeable_id")
    GithubUserInfo githubUserInfo;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    Chatroom chatroom;

    @Column(columnDefinition = "DATETIME")
    LocalDateTime createdAt;

    public Message() {
    }

    public Message(String text, GithubUserInfo githubUserInfo, Chatroom chatroom, LocalDateTime createdAt) {
        this.text = text;
        this.githubUserInfo = githubUserInfo;
        this.chatroom = chatroom;
        this.createdAt = createdAt;
    }

    public static Message of(String text, GithubUserInfo githubUserInfo, Chatroom chatroom, LocalDateTime createdAt) {
        return new Message(text, githubUserInfo, chatroom, createdAt);
    }
}
