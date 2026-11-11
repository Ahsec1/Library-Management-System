package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.config.DB;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class LibraryManagementSystemApplication {

    public static void main(String[] args){

        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        try (var connection =  DB.connect()){
            System.out.println("Connected to the PostgreSQL database.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return;
        }

        while (running) {

            printMenu();

            int choice = checkInt(scanner, "Choose an option: ");

            int memberId;
            int bookId;

            try {

                switch (choice) {

                    case 1:
                        autofillLibrary(library);
                        break;

                    case 2:
                        memberId = checkInt(scanner, "Member ID: ");
                        bookId = checkInt(scanner, "Book ID: ");

                        if (library.borrowBooks(memberId, bookId)) {
                            System.out.println("Book borrowed successfully.");
                        }

                        break;

                    case 3:
                        memberId = checkInt(scanner, "Member ID: ");
                        bookId = checkInt(scanner, "Book ID: ");

                        if (library.returnBook(memberId, bookId)) {
                            System.out.println("Book returned successfully.");
                        }

                        break;

                    case 4:
                        library.load();
                        break;

                    case 0:
                        running = false;
                        System.out.println("Exiting Program...");
                        break;

                    default:
                        System.out.println("Invalid option.");
                        break;
                }

            } catch (SQLException e) {

                System.out.println("Database Error: " + e.getMessage());

            }

            if (running) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printMenu() {

        System.out.println();
        System.out.println("===== LIBRARY MENU =====");
        System.out.println("1. Add sample members and books");
        System.out.println("2. Borrow book");
        System.out.println("3. Return book");
        System.out.println("4. Display library records");
        System.out.println("0. Exit");
    }

    private static void autofillLibrary(Library library) throws SQLException {

        if (!library.isEmpty()) {
            System.out.println(
                    "Library already has data. Sample data not added."
            );
            return;
        }

        library.registerMember(
                new Member("John", "john@mail.com")
        );

        library.registerMember(
                new Member("Michael", "michael@mail.com")
        );

        library.registerMember(
                new Member("Villagomez", "villagomez@mail.com")
        );

        library.addBook(
                new Book(
                        "Percy Jackson and the Lightning Thief",
                        "Rick Riordan",
                        "Miramax Books",
                        "2005"
                )
        );

        library.addBook(
                new Book(
                        "Harry Potter and the Sorcerer's Stone",
                        "J.K. Rowling",
                        "Scholastic",
                        "1998"
                )
        );

        library.addBook(
                new Book(
                        "The Hunger Games",
                        "Suzanne Collins",
                        "Scholastic Press",
                        "2008"
                )
        );

        library.addBook(
                new Book(
                        "The Hobbit",
                        "J.R.R. Tolkien",
                        "George Allen & Unwin",
                        "1937"
                )
        );

        library.addBook(
                new Book(
                        "Diary of a Wimpy Kid",
                        "Jeff Kinney",
                        "Amulet Books",
                        "2007"
                )
        );
        System.out.println("Sample data added successfully.");
    }

    private static int checkInt(Scanner scanner, String str) {

        while (true) {
            System.out.print(str);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
