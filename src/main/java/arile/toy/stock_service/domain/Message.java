package arile.toy.stock_service.domain;

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
}
