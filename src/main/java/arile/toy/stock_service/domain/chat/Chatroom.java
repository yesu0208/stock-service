package arile.toy.stock_service.domain.chat;

import arile.toy.stock_service.domain.GithubUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(
        name = "chatrooms",
        indexes = {
                @Index(name = "idx_chatrooms_stock_name", columnList = "stock_name"),
                @Index(name = "idx_chatrooms_unchangeable_id", columnList = "unchangeable_id")
        }
)
@Entity
public class Chatroom {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    @Column(name = "title", nullable = false)
    private String title;

    @ToString.Exclude
    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GithubUserChatroomMapping> githubUserChatroomMappings = new HashSet<>();

    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "unchangeable_id", nullable = false)
    private String unchangeableId;

    @Setter
    @Transient
    private Boolean hasNewMessage;


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


    // 테스트용
    public Chatroom(Long chatroomId, String title, Set<GithubUserChatroomMapping> githubUserChatroomMappings,
                    LocalDateTime createdAt, String stockName, String createdBy, String unchangeableId) {
        this.chatroomId = chatroomId;
        this.title = title;
        this.githubUserChatroomMappings = githubUserChatroomMappings;
        this.createdAt = createdAt;
        this.stockName = stockName;
        this.createdBy = createdBy;
        this.unchangeableId = unchangeableId;
    }

    public static Chatroom of(Long chatroomId, String title, Set<GithubUserChatroomMapping> githubUserChatroomMappings, LocalDateTime createdAt, String stockName,
                              String createdBy, String unchangeableId) {
        return new Chatroom(chatroomId, title, githubUserChatroomMappings, createdAt, stockName, createdBy, unchangeableId);
    }


    public GithubUserChatroomMapping addGithubUserInfo(GithubUserInfo githubUserInfo) {

        GithubUserChatroomMapping githubUserChatroomMapping =
                GithubUserChatroomMapping.of(githubUserInfo, this);

        this.githubUserChatroomMappings.add(githubUserChatroomMapping);

        return  githubUserChatroomMapping;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Chatroom that)) return false;

        if (getChatroomId() == null) {
            return Objects.equals(this.getTitle(), that.getTitle()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getStockName(), that.getStockName()) &&
                    Objects.equals(this.getCreatedBy(), that.getCreatedBy()) &&
                    Objects.equals(this.getUnchangeableId(), that.getUnchangeableId());
        }

        return Objects.equals(this.getChatroomId(), that.getChatroomId());
    }


    @Override
    public int hashCode() {
        if (getChatroomId() == null) {
            return Objects.hash(getTitle(), getCreatedAt(), getStockName(), getCreatedBy(), getUnchangeableId());
        }
        return Objects.hash(getChatroomId());
    }
}
