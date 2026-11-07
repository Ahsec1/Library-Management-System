CREATE DATABASE library_db;
\c library_db

CREATE TABLE books (
    id SERIAL PRIMARY KEY,title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(150),
    copyright_date VARCHAR(10),
    available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE loan (
    id SERIAL PRIMARY KEY,
    member_id INT NOT NULL REFERENCES members(id),
    book_id INT NOT NULL REFERENCES books(id),
    borrowed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    returned_at TIMESTAMP
);