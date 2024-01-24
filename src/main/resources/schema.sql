CREATE TABLE if not exists books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(255) ,
    title VARCHAR(255) ,
    author VARCHAR(255) ,
    edition VARCHAR(255),
    status VARCHAR(255) DEFAULT 'AVAILABLE',
    username VARCHAR(255) DEFAULT 'NONE',
    price INT
);

--ALTER TABLE users alter credits int default 3;
