# Library-Management-System
A library management system that can borrow and return books with exceptions

## Features

- Add sample members and books
- Borrow a book (member and book are validated)
- Return a book
- Display books, members (with their borrowed books) and current loans
- Custom exceptions: `MemberNotFoundException`, `BookNotFoundException`, `BookNotAvailableException`
- Availability check (`UPDATE ... WHERE available = TRUE`) so a book cannot be borrowed twice

## Project Structure

```
db/
  schema.sql
src/main/java/com/example/librarymanagementsystem/
  config/            DB, DatabaseConfig
  Book.java
  Member.java
  Library.java
  BookNotFoundException.java
  MemberNotFoundException.java
  BookNotAvailableException.java
  LibraryManagementSystemApplication.java
src/main/resources/
  application.properties
  db.properties.example
```

## Database Schema

Three tables, defined in `db/schema.sql`:

- `books` (`PK` id, title, author, publisher, copyright_date, available)
- `members` (`PK` id, name, email (unique))
- `loan` (id, `FK` member_id, `FK` book_id, borrowed_at, returned_at)


### The app shows a menu:

```
1. Add sample members and books
2. Borrow book
3. Return book
4. Display library records
0. Exit
```