package arile.toy.stock_service.domain;

import jakarta.persistence.*;

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
}
