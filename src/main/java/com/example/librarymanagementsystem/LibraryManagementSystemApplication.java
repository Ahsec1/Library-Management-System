package com.example.librarymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class LibraryManagementSystemApplication {

    public static void main(String[] args) throws BookNotAvailableException, IOException {
        Library library = new Library("data");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = checkInt(scanner, "Choose an option: ");
            int memberId;
            int bookId;

            switch (choice) {
                case 1:
                    autofillLibrary(library);
                    break;

                case 2:
                    memberId = checkInt(scanner, "Member ID: ");
                    bookId = checkInt(scanner, "Book ID: ");
                    if (library.borrowBooks(memberId, bookId)) {
                        System.out.println("Book borrowed.");
                    }

                        break;
                case 3:
                    memberId = checkInt(scanner, "Member ID: ");
                    bookId = checkInt(scanner, "Book ID: ");
                    library.returnBook(memberId, bookId);
                    System.out.println("Return processed.");

                    break;
                case 4:
                    library.save();
                    System.out.println("Saved.");

                    break;
                case 5:
                    library.load();
                    System.out.println("Loaded.");

                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting Program..");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
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
        System.out.println("4. Save");
        System.out.println("5. Load");
        System.out.println("0. Exit");
    }

    private static void autofillLibrary(Library library) {
        if (!library.isEmpty()) {
            System.out.println("Library already has data. Sample data not added.");
            return;
        }

        library.registerMember(new Member(1, "John", "john@mail.com"));
        library.registerMember(new Member(2, "Michael", "michael@mail.com"));
        library.registerMember(new Member(3, "Villagomez", "villagomez@mail.com"));

        library.addBook(new Book(101, "Percy Jackson and the Lightning Thief", "Rick Riordan", "Miramax Books", "2005"));
        library.addBook(new Book(102, "Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Scholastic", "1998"));
        library.addBook(new Book(103, "The Hunger Games", "Suzanne Collins", "Scholastic Press", "2008"));
        library.addBook(new Book(104, "The Hobbit", "J.R.R. Tolkien", "George Allen & Unwin", "1937"));
        library.addBook(new Book(105, "Diary of a Wimpy Kid", "Jeff Kinney", "Amulet Books", "2007"));

        System.out.println("Sample data added.");
    }

    private static int checkInt(Scanner scanner, String str) {
        while (true) {
            System.out.print(str);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
