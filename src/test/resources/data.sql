-- 예제 사용자 (**)
insert into github_user_information (unchangeable_id, id, name, email, last_login_at, fee)
values ('123456', 'test-id', 'test-name', 'test@email.com', now(), 0.01)
;

-- 예제 게시물 (**)
insert into posts (post_id, unchangeable_id, stock_name, body, replies_count, likes_count, dislikes_count, title,
created_at, modified_at)
values
(1, '123456', '삼성전자', '내용 1', 0, 1, 2, '제목 1', now(), now()),
(2, '123456', 'SK하이닉스', '내용 2', 0, 1, 2, '제목 2', now(), now());

-- 예제 싫어요 (**)
insert into dislikes (dislike_id, unchangeable_id, post_id)
values (1, '123456', 1);

-- 예제 좋아요 (**)
insert into likes (like_id, unchangeable_id, post_id)
values (1, '123456', 2);


-- 예제 댓글
insert into replies (reply_id, post_id, unchangeable_id, body, created_at, modified_at)
values
(1, 1, '123456', '댓글 내용 1', now(), now()),
(2, 1, '123456', '댓글 내용 2', now(), now())
;

-- 예제 관심 그룹
insert into interest_groups (interest_group_id, group_name, unchangeable_id, created_at, created_by, modified_at, modified_by)
values
(1, 'group_name1', '123456', now(), 'test-name', now(), 'test-name')
;

-- 예제 관심 종목
insert into interest_stocks (interest_stock_id, interest_group_id, stock_name, buying_price, num_of_stocks, break_even_price, field_order,
 total_buying_price, created_at, created_by, modified_at, modified_by)
values
(1, 1, '삼성전자', 50000, 50, 50100, 1, 2500000, now(), 'test-name', now(), 'test-name'),
(2, 1, 'SK하이닉스', 200000, 50, 202000, 2, 10000000, now(), 'test-name', now(), 'test-name')
;

-- 예제 채팅방
insert into chatrooms (chatroom_id, created_at, created_by, stock_name, title, unchangeable_id)
values
(1, now(), now(), '삼성전자', '채팅방 제목 1', '123456')
;

-- 예제 메시지
insert into messages (message_id, chatroom_id, unchangeable_id, created_at, text, message_type)
values
(1, 1, '123456', DATE '2020-02-02', '메시지 내용 1', 'USER'),
(2, 1, '123456', DATE '2020-02-02', '메시지 내용 2', 'USER')
;

-- 예제 매핑 테이블
insert into github_user_chatroom_mappings (github_user_chatroom_mapping_id, chatroom_id, unchangeable_id, last_checked_at)
values
(1, 1, '123456', DATE '2020-02-02')
;
