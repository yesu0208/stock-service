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

---- 예제 관심 그룹
--insert into interest_groups (id, group_name, unchangeable_id, created_at, created_by, modified_at, modified_by)
--values
--(1, 'group_name', '123456', now(), 'Arile', now(), 'Arile')
--;
--
---- 예제 관심 종목
--insert into interest_stocks (id, interest_group_id, stock_name, buying_price, num_of_stocks, break_even_price, field_order,
-- total_buying_price, created_at, created_by, modified_at, modified_by)
--values
--(1, 1, '삼성전자', 50000, 50, 50100, 1, 2500000, now(), 'Arile', now(), 'Arile'),
--(2, 1, '삼성전자', 50000, 50, 50100, 2, 2500000, now(), 'Arile', now(), 'Arile')
--;