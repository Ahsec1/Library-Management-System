CREATE DATABASE library_db;
\c library_db

CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE books (
    id SERIAL PRIMARY KEY,title VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL
    author_id INT NOT NULL REFERENCES authors(id),
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

-- Creating indexes for `Foreign Keys` as they were not automatically added in PostgreSQL, only the primary keys are indexed shown in the psql --
CREATE INDEX idx_loan_member_id ON loan(member_id);
CREATE UNIQUE INDEX uq_loan_book ON loan(book_id) WHERE returned_at IS NULL;
CREATE INDEX idx_books_author_id ON books(author_id);
