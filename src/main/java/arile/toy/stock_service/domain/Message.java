package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.constant.MessageType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column
    @Enumerated(EnumType.STRING)
    MessageType messageType;

    public Message() {
    }

    public Message(String text, GithubUserInfo githubUserInfo, Chatroom chatroom, LocalDateTime createdAt, MessageType messageType) {
        this.text = text;
        this.githubUserInfo = githubUserInfo;
        this.chatroom = chatroom;
        this.createdAt = createdAt;
        this.messageType = messageType;
    }

    public static Message of(String text, GithubUserInfo githubUserInfo, Chatroom chatroom, LocalDateTime createdAt, MessageType messageType) {
        return new Message(text, githubUserInfo, chatroom, createdAt, messageType);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Message that)) return false;

        if (getMessageId() == null) {
            return Objects.equals(this.getText(), that.getText()) &&
                    Objects.equals(this.getGithubUserInfo(), that.getGithubUserInfo()) &&
                    Objects.equals(this.getChatroom(), that.getChatroom()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getMessageType(), that.getMessageType());
        }

        return Objects.equals(this.getMessageId(), that.getMessageId());
    }

    @Override
    public int hashCode() {
        if (getMessageId() == null) {
            return Objects.hash(getText(), getGithubUserInfo(), getChatroom(), getCreatedAt(), getMessageType());
        }
        return Objects.hash(getMessageId());
    }
}
