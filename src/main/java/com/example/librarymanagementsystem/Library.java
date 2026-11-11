package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.config.DB;
import java.sql.SQLException;
import java.sql.*;

public class Library {

    public boolean isEmpty() throws SQLException {

        String sql = """
                    SELECT
                    (SELECT COUNT(*) FROM books) +
                    (SELECT COUNT(*) FROM members) AS total
                    """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            return rs.next() && rs.getInt("total") == 0;
        }
    }

    public void addBook (Book book) throws SQLException {
        String sql = """
                INSERT INTO books
                (title, author, publisher, copyright_date, available)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getCopyrightDate());
            pstmt.setBoolean(5, book.isAvailable());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getInt(1));
                }
            }

            System.out.println("Book successfully added!");
        }
    }

    public void registerMember(Member member) throws SQLException{

        String sql = """
                    INSERT INTO members (name, email)
                    VALUES (?, ?)
                    """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    member.setId(rs.getInt(1));
                }
            }

            System.out.println("Member successfully registered!");
        }
    }

    public boolean borrowBooks(int memberId, int bookId) throws SQLException {

        try (Connection conn = DB.connect()) {
            conn.setAutoCommit(false);

            try {
                if (!memberExists(conn, memberId)) {
                    throw new MemberNotFoundException("Member Not Found: " + memberId);
                }
                if (!markUnavailable(conn, bookId)) {
                    if (!bookExists(conn, bookId)) {
                        throw new BookNotFoundException("Book Not Found: " + bookId);
                    }
                    throw new BookNotAvailableException("Book Already Borrowed: " + bookId);
                }

                insertLoan(conn, memberId, bookId);
                conn.commit();
                return true;

            } catch (MemberNotFoundException | BookNotFoundException | BookNotAvailableException e) {
                conn.rollback();
                System.out.println("Error - " + e.getMessage());
                return false;

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private boolean memberExists(Connection conn, int memberId) throws SQLException {
        String sql = """
                    SELECT 1
                    FROM members
                    WHERE id = ?
                    """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean bookExists(Connection conn, int bookId) throws SQLException {
        String sql = """
                    SELECT 1
                    FROM books
                    WHERE id = ?
                    """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean markUnavailable(Connection conn, int bookId) throws SQLException {
        String sql = """
                    UPDATE books
                    SET available = FALSE
                    WHERE id = ? AND available = TRUE
                    """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private void insertLoan(Connection conn, int memberId, int bookId) throws SQLException {
        String sql = """
                    INSERT INTO loan (member_id, book_id)
                    VALUES (?, ?)
                    """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }

    public boolean returnBook(int memberId, int bookId)
            throws SQLException {

        String deleteLoan = """
                DELETE FROM loan
                WHERE member_id = ? AND book_id = ?
                """;

        String updateBook = """
                UPDATE books
                SET available = TRUE
                WHERE id = ?
                """;

        try (Connection conn = DB.connect()) {
            conn.setAutoCommit(false);

            try {
                int deletedRows;

                try (PreparedStatement pstmt =
                             conn.prepareStatement(deleteLoan)) {

                    pstmt.setInt(1, memberId);
                    pstmt.setInt(2, bookId);

                    deletedRows = pstmt.executeUpdate();
                }

                if (deletedRows == 0) {
                    conn.rollback();
                    System.out.println(
                            "No borrowing record found."
                    );
                    return false;
                }

                try (PreparedStatement pstmt =
                             conn.prepareStatement(updateBook)) {
                    pstmt.setInt(1, bookId);
                    pstmt.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void loadBooks() throws SQLException {

        String sql = """
                SELECT *
                FROM books
                ORDER BY id
                """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("------- BOOKS IN THE LIBRARY -------");

            while (rs.next()) {

                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("copyright_date")
                );

                book.setId(rs.getInt("id"));
                book.setAvailable(rs.getBoolean("available"));

                System.out.println(book);
                System.out.println(
                        "Available: " + book.isAvailable()
                );
            }
        }
    }

    public void loadMembers() throws SQLException {

        String sql = """
                SELECT *
                FROM members
                ORDER BY id
                """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("------- MEMBERS REGISTERED -------");

            while (rs.next()) {
                Member member = new Member(
                        rs.getString("name"),
                        rs.getString("email")
                );

                member.setId(rs.getInt("id"));
                loadBorrowedBooks(conn, member);
                System.out.println(member);
            }
        }
    }

    private void loadBorrowedBooks(Connection conn, Member member) throws SQLException {
        String sql = """
                    SELECT b.id, b.title, b.author, b.publisher, b.copyright_date
                    FROM loan l
                    JOIN books b ON l.book_id = b.id
                    WHERE l.member_id = ?
                    ORDER BY b.id
                    """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("copyright_date"));
                    book.setId(rs.getInt("id"));
                    book.setAvailable(false);
                    member.getBorrowedBooks().add(book);
                }
            }
        }
    }

    public void loadLoan() throws SQLException {

        String sql = """
                SELECT
                    m.id AS member_id,
                    m.name AS member_name,
                    b.id AS book_id,
                    b.title AS book_title
                FROM loan l
                JOIN members m
                    ON l.member_id = m.id
                JOIN books b
                    ON l.book_id = b.id
                ORDER BY m.id, b.id
                """;

        try (Connection conn = DB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n------- LOAN BOOKS -------");
            while (rs.next()) {
                System.out.println(
                        "Member ID: " + rs.getInt("member_id")
                        + " | " + rs.getString("member_name")
                        + " | Book ID: " + rs.getInt("book_id")
                        + " | " + rs.getString("book_title")
                );
            }
        }
    }

    public void load() throws SQLException {

        loadBooks();
        loadMembers();
        loadLoan();
    }
}
