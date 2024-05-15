INSERT INTO users (username, email, password, profile_picture_url, bio, role)
VALUES
    ('Test', 'test@test.com', '$2a$10$XHhlg3g6PwhkfEtdkxyF9.7fq9NpTop6wSYf8de2kBCxPJbejACqe', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png', 'User Test', 'ROLE_USER'),
    ('Test1', 'test1@test1.com', '$2a$10$5ihF4worqanXfI9Wor3TXe1gaXTA5sq5aNdZY1d1maqSA1BGZkAha', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png', 'User Test1', 'ROLE_USER'),
    ('Test2', 'test2@test2.com', '$2a$10$DwERm9e4NOwYIoJ8UnGYGOw93Zo0FbuqcY/Kh4LROwso43a5ttIFG', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png', 'User Test2', 'ROLE_USER'),
    ('Test3', 'test3@test3.com', '$2a$10$vPEyk8e4zQObj3FHFe87XeLPbFNJz25swU6VHXVJXbfkX2Vp5HpvK', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png', 'User Test3', 'ROLE_USER'),
    ('admin', 'admin@admin.com', '$2a$10$KuD57DnnTv4SPtZUXBREfOlzpDkdAbI4olH4LVu8QAFhoY66SMwPu', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png', 'Administrator', 'ROLE_ADMIN');

INSERT INTO photos (user_id, title, description, image_url, uploaded_at)
VALUES
    (5, 'Telecaster', 'Cool new pickguard', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/c201a10f-4ea3-4fd5-a4a1-27b885cf65d6.png', '2024-05-15 01:55:34'),
    (5, 'Apollo', 'My African Grey!', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/8e365b32-c086-44c0-850c-64abfe0ced2d.jpg', '2024-05-15 02:00:18'),
    (5, 'Williams at Monaco!', 'Just took this pic yesterday', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/0b8f831d-6e0c-457a-89ef-29b1a98d5a00.png', '2024-05-15 02:04:24'),
    (5, 'Mano katukas Kipriukas', 'Nu jis labai mielas', 'https://photo-ai-bak.s3.eu-north-1.amazonaws.com/0b6ddbf2-44f0-4308-ba85-c002fb44458d.jpg', '2024-05-15 02:49:01');

INSERT INTO tag (id, name)
VALUES
    (1, 'electric_guitar'),
    (2, 'African_grey'),
    (3, 'racer'),
    (4, 'Siamese_cat');

INSERT INTO photo_tag (photo_id, tag_id, confidence)
VALUES
    (1, 1, 0.99),
    (2, 2, 1.00),
    (3, 3, 0.82),
    (4, 4, 0.99);

-- V2__Insert_device_catalog.sql

INSERT INTO device_catalog (type, model)
VALUES
('Phone', 'Apple iPhone 13 Pro'),
('Phone', 'Samsung Galaxy S21 Ultra'),
('Phone', 'Google Pixel 6 Pro'),
('Phone', 'OnePlus 9 Pro'),
('Phone', 'Xiaomi Mi 11 Ultra'),
('Phone', 'Sony Xperia 1 III'),
('Phone', 'Oppo Find X3 Pro'),
('Phone', 'Asus ROG Phone 5'),
('Phone', 'Huawei P50 Pro'),
('Phone', 'Motorola Edge Plus'),
('Phone', 'Nokia 8.3 5G'),
('Phone', 'Apple iPhone SE (2020)'),
('Phone', 'Samsung Galaxy Note 20 Ultra'),
('Phone', 'Google Pixel 5'),
('Phone', 'LG Velvet'),

('Camera', 'Canon EOS R5'),
('Camera', 'Nikon Z7 II'),
('Camera', 'Sony Alpha 1'),
('Camera', 'Fujifilm X-T4'),
('Camera', 'Panasonic Lumix S1R'),
('Camera', 'Leica Q2'),
('Camera', 'Olympus OM-D E-M1 Mark III'),
('Camera', 'Canon EOS R6'),
('Camera', 'Nikon D850'),
('Camera', 'Sony Alpha 7R IV'),
('Camera', 'Pentax K-1 Mark II'),
('Camera', 'Canon EOS 90D'),
('Camera', 'Nikon Z6 II'),
('Camera', 'Sony Alpha 7S III'),
('Camera', 'Panasonic Lumix GH5');
