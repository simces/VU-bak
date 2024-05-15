CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
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
);


CREATE TABLE follows (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         follower_id BIGINT NOT NULL,
                         following_id BIGINT NOT NULL,
                         UNIQUE KEY unique_follow_relationship (follower_id, following_id)
);


CREATE TABLE tag (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL
);

CREATE TABLE photo_tag (
                          photo_id BIGINT NOT NULL,
                          tag_id BIGINT NOT NULL,
                          confidence DECIMAL(10, 9),
                          PRIMARY KEY (photo_id, tag_id),
                          FOREIGN KEY (photo_id) REFERENCES photos(id),
                          FOREIGN KEY (tag_id) REFERENCES tag(id)
);

CREATE TABLE audits (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  admin_id BIGINT NOT NULL,
                                  action_type VARCHAR(50) NOT NULL,
                                  table_name VARCHAR(50) NOT NULL,
                                  record_id BIGINT NOT NULL,
                                  data_before JSON NULL,
                                  data_after JSON NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);