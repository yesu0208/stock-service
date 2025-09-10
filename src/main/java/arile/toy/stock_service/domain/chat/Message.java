package arile.toy.stock_service.domain.chat;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.constant.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(name = "messages")
@Entity
public class Message {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @JoinColumn(name = "unchangeable_id", nullable = false)
    @ManyToOne(optional = false)
    private GithubUserInfo githubUserInfo;

    @JoinColumn(name = "chatroom_id", nullable = false)
    @ManyToOne(optional = false)
    private Chatroom chatroom;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "message_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;


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
