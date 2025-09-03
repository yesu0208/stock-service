package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;

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

    public Message() {
    }

    public Message(String text, GithubUserInfo githubUserInfo, Chatroom chatroom) {
        this.text = text;
        this.githubUserInfo = githubUserInfo;
        this.chatroom = chatroom;
    }

    public static Message of(String text, GithubUserInfo githubUserInfo, Chatroom chatroom) {
        return new Message(text, githubUserInfo, chatroom);
    }
}
