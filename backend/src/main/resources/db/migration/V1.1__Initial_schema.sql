CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       profile_picture_url VARCHAR(255),
                       bio TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE photos (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        title VARCHAR(100),
                        description TEXT,
                        image_url VARCHAR(255) NOT NULL,
                        uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE likes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        photo_id BIGINT NOT NULL,
                        user_id  BIGINT NOT NULL,
                        FOREIGN KEY (photo_id) REFERENCES photos (id),
                        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE comments (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        photo_id BIGINT NOT NULL,
                        comment TEXT,
                        commented_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users (id),
                        FOREIGN KEY (photo_id) REFERENCES photos (id)
)

CREATE TABLE notifications (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        type VARCHAR(50),
                        type_id BIGINT NOT NULL,
                        message TEXT,
                        read_status BIT NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users (id)
)

CREATE TABLE follows (
                         follower_id BIGINT NOT NULL,
                         following_id BIGINT NOT NOT NULL,
                         PRIMARY KEY (follower_id, following_id),
                         FOREIGN KEY (follower_id) REFERENCES users (id),
                         FOREIGN KEY (following_id) REFERENCES users (id)
);



