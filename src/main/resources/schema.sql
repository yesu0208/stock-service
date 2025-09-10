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

CREATE TABLE github_user_information (
    unchangeable_id VARCHAR(255) PRIMARY KEY,
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    last_login_at DATETIME NOT NULL,
    fee DOUBLE NOT NULL
);

CREATE TABLE static_stock_information (
    stock_name VARCHAR(255) PRIMARY KEY,
    short_code VARCHAR(255) NOT NULL,
    market_class ENUM('KONEX','KOSDAQ','KOSDAQGLOBAL','KOSPI') NOT NULL
);

CREATE TABLE posts (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stock_name VARCHAR(255) NOT NULL,
    unchangeable_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    replies_count BIGINT NOT NULL,
    likes_count BIGINT NOT NULL,
    dislikes_count BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_posts_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_posts_stock_name
        FOREIGN KEY (stock_name)
        REFERENCES static_stock_information(stock_name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE interest_groups (
    interest_group_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_at DATETIME NOT NULL,
    modified_by VARCHAR(255) NOT NULL,
    group_name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_interest_groups_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE chatrooms (
    chatroom_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    stock_name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    unchangeable_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_chatrooms_unchangeable_id
        FOREIGN KEY (unchangeable_id)
        REFERENCES github_user_information(unchangeable_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_chatrooms_stock_name
        FOREIGN KEY (stock_name)
        REFERENCES static_stock_information(stock_name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE replies (
    reply_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    unchangeable_id VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
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

CREATE TABLE likes (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255) NOT NULL,
    post_id BIGINT NOT NULL,
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

CREATE TABLE dislikes (
    dislike_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unchangeable_id VARCHAR(255) NOT NULL,
    post_id BIGINT NOT NULL,
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
        ON UPDATE CASCADE,
    CONSTRAINT fk_interest_stocks_stock_name
        FOREIGN KEY (stock_name)
        REFERENCES static_stock_information(stock_name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE github_user_chatroom_mappings (
    github_user_chatroom_mapping_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chatroom_id BIGINT NOT NULL,
    unchangeable_id VARCHAR(255) NOT NULL,
    last_checked_at DATETIME NOT NULL,
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

CREATE TABLE messages (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chatroom_id BIGINT NOT NULL,
    unchangeable_id VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    text TEXT NOT NULL,
    message_type ENUM('SYSTEM', 'USER') NOT NULL,
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