INSERT INTO users (username, email, password, profile_picture_url, bio)
VALUES
    ('john_doe', 'john@example.com', 'password123', 'http://example.com/john.jpg', 'Photography enthusiast.'),
    ('jane_doe', 'jane@example.com', 'password123', 'http://example.com/jane.jpg', 'Professional photographer.');


INSERT INTO photos (user_id, title, description, image_url)
VALUES
    (1, 'Sunset', 'Beautiful sunset at the beach.', 'http://example.com/photo1.jpg'),
    (2, 'Mountains', 'Majestic mountains in the morning.', 'http://example.com/photo2.jpg');


INSERT INTO likes (photo_id, user_id)
VALUES
    (1, 2),
    (2, 1);


INSERT INTO comments (user_id, photo_id, comment)
VALUES
    (2, 1, 'Amazing shot!'),
    (1, 2, 'Love the colors!');


INSERT INTO notifications (user_id, type, type_id, message)
VALUES
    (1, 'like', 1, 'Jane liked your photo'),
    (2, 'comment', 2, 'John commented on your photo');


INSERT INTO follows (follower_id, following_id)
VALUES
    (1, 2),
    (2, 1);


INSERT INTO AiTagFeedback (photo_tag_id, user_id, feedback)
VALUES
    (1, 1, 1),
    (2, 2, 0);


INSERT INTO tag (name)
VALUES
    ('Siamese_cat'),
    ('Persian_cat');


INSERT INTO PhotoTag (photo_id, tag_id, confidence)
VALUES
    (1, 1, 0.9999690055847168),
    (2, 2, 0.9000000000000000);
