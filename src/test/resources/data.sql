-- 예제 사용자
insert into github_user_information (id, unchangeable_id, name, email, fee, last_login_at)
values ('id', '123456', 'Arile', 'Arile@email.com', 0.01, now())
;

-- 예제 관심 그룹
insert into interest_groups (id, group_name, unchangeable_id, created_at, created_by, modified_at, modified_by)
values
(1, 'group_name', '123456', now(), 'Arile', now(), 'Arile')
;

-- 예제 관심 종목
insert into interest_stocks (id, interest_group_id, stock_name, buying_price, num_of_stocks, break_even_price, field_order,
 total_buying_price, created_at, created_by, modified_at, modified_by)
values
(1, 1, '삼성전자', 50000, 50, 50100, 1, 2500000, now(), 'Arile', now(), 'Arile'),
(2, 1, '삼성전자', 50000, 50, 50100, 2, 2500000, now(), 'Arile', now(), 'Arile')
;

-- 예제 게시물
insert into posts (post_id, unchangeable_id, stock_name, body, replies_count, likes_count, dislikes_count, title,
created_at, modified_at)
values (1, '123456', '삼성전자', '내용', 0, 1, 2, '제목', now(), now());

-- 예제 댓글
insert into replies (reply_id, post_id, unchangeable_id, body, created_at, modified_at)
values
(1, 1, '123456', '내용 1', now(), now()),
(2, 1, '123456', '내용 2', now(), now())
;