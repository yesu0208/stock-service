package arile.toy.stock_service.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

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
    Set<GithubUserChatroomMapping> githubUserChatroomMappings;

    @Column(columnDefinition = "DATETIME")
    LocalDateTime createdAt;
}
