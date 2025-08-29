package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Reply;
import arile.toy.stock_service.domain.post.Post;

import java.time.LocalDateTime;

public record ReplyDto(
        Long replyId,
        String body,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String name,
        String unchangeableId
) {
    // Entity -> Dto
    public static ReplyDto fromEntity(Reply entity) {
        return new ReplyDto(
                entity.getReplyId(),
                entity.getBody(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getUser().getName(),
                entity.getUser().getUnchangeableId()
        );
    }

    // static method (전체)
    public static ReplyDto of(
            Long replyId,
            String body,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            String name,
            String unchangeableId
    ) {
        return new ReplyDto(replyId, body, createdAt, modifiedAt, name, unchangeableId);
    }

    // static method (일부)
    public static ReplyDto of(String body, String name, String unchangeableId) {
        return new ReplyDto(null, body, null, null, name, unchangeableId);
    }


    // Dto -> Entity
    public Reply createEntity(GithubUserInfo user, Post post) {
        post.setRepliesCount(post.getRepliesCount() + 1);
        return Reply.of(body, user, post);
    }

    // Dto -> Entity
    public Reply updateEntity(Reply entity) {
        entity.setBody(body);

        return entity;
    }
}