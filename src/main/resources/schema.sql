DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS github_user_chatroom_mappings;
DROP TABLE IF EXISTS replies;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS dislikes;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS chatrooms;
DROP TABLE IF EXISTS interest_stocks;
DROP TABLE IF EXISTS interest_groups;
DROP TABLE IF EXISTS github_user_information;
DROP TABLE IF EXISTS static_stock_information;

-- 1️⃣ 기본 테이블
CREATE TABLE github_user_information (
    unchangeable_id VARCHAR(255) PRIMARY KEY,
    id VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255),
    last_login_at DATETIME,
    fee DOUBLE
);

CREATE TABLE static_stock_information (
    static_stock_info_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_code VARCHAR(255),
    stock_name VARCHAR(255),
    market_class ENUM('KONEX','KOSDAQ','KOSDAQGLOBAL','KOSPI')
);

-- 2️⃣ posts (replies, likes, dislikes 참조 대상)
CREATE TABLE posts (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stock_name VARCHAR(255),
    unchangeable_id VARCHAR(255),
    title VARCHAR(255),
    body TEXT,
    replies_count BIGINT,
    likes_count BIGINT,
    dislikes_count BIGINT,
    created_at DATETIME,
    modified_at DATETIME,
    CONSTRAINT fk_posts_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 3️⃣ interest_groups (interest_stocks 참조 대상)
CREATE TABLE interest_groups (
    interest_group_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255),
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255) NOT NULL,
    group_name VARCHAR(255),
    CONSTRAINT fk_interest_groups_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 4️⃣ chatrooms (github_user_chatroom_mappings, messages 참조 대상)
CREATE TABLE chatrooms (
    chatroom_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME,
    created_by VARCHAR(255),
    stock_name VARCHAR(255),
    title VARCHAR(255),
    unchangeable_id VARCHAR(255),
    CONSTRAINT fk_chatrooms_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 5️⃣ replies
CREATE TABLE replies (
    reply_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT,
    unchangeable_id VARCHAR(255),
    body TEXT,
    created_at DATETIME,
    modified_at DATETIME,
    CONSTRAINT fk_replies_post_id
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_replies_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 6️⃣ likes
CREATE TABLE likes (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255),
    post_id BIGINT,
    CONSTRAINT fk_likes_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_likes_post_id
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 7️⃣ dislikes
CREATE TABLE dislikes (
    dislike_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255),
    post_id BIGINT,
    CONSTRAINT fk_dislikes_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_dislikes_post_id
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 8️⃣ interest_stocks
CREATE TABLE interest_stocks (
    interest_stock_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interest_group_id BIGINT NOT NULL,
    stock_name VARCHAR(255) NOT NULL,
    buying_price INT,
    num_of_stocks INT,
    break_even_price INT,
    total_buying_price INT,
    field_order INT NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255) NOT NULL,
    CONSTRAINT fk_interest_stocks_interest_group_id
        FOREIGN KEY (interest_group_id)
        REFERENCES interest_groups(interest_group_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 9️⃣ github_user_chatroom_mappings
CREATE TABLE github_user_chatroom_mappings (
    github_user_chatroom_mapping_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chatroom_id BIGINT,
    unchangeable_id VARCHAR(255),
    last_checked_at DATETIME,
    CONSTRAINT fk_github_user_chatroom_mappings_chatroom_id
        FOREIGN KEY (chatroom_id)
        REFERENCES chatrooms(chatroom_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_github_user_chatroom_mappings_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 🔟 messages
CREATE TABLE messages (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chatroom_id BIGINT,
    unchangeable_id VARCHAR(255),
    created_at DATETIME,
    text TEXT,
    message_type ENUM('SYSTEM', 'USER'),
    CONSTRAINT fk_messages_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_messages_chatroom_id
        FOREIGN KEY (chatroom_id)
        REFERENCES chatrooms(chatroom_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);