package com.example.librarymanagementsystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    private Path booksFile;
    private Path membersFile;
    private Path checkoutFile;

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();

    public Library(String baseFolder){
        Path dir = Path.of(baseFolder);
        this.booksFile = dir.resolve("books.txt");
        this.membersFile = dir.resolve("members.text");
        this.checkoutFile = dir.resolve("checkout.txt");
    }

    public boolean isEmpty() {
        return books.isEmpty() && members.isEmpty();
    }

    public void addBook (Book book) {
        books.put(book.getId(), book);
    }

    public void registerMember(Member member){
        members.put(member.getId(), member);
    }

    public boolean borrowBooks (int memberId, int bookId) {
        try{
            Member member = members.get(memberId);
            Book book = books.get(bookId);

            if(member == null){
                throw new MemberNotFoundException("Member Not Found: " + memberId);
            }
            if(book == null){
                throw new BookNotFoundException("Book Not Found [" + bookId + "]");
            }
            if(!book.isAvailable()){
                throw new BookNotAvailableException("ID: " + bookId + " - Book Already Borrowed: " + book.getTitle());
            }
            book.setAvailable(false);
            member.getBorrowedBooks().add(book);
        }catch (MemberNotFoundException | BookNotFoundException | BookNotAvailableException e){
            System.out.println("Error - " + e.getMessage());
        }
        return false;
    }

    public void returnBook (int memberId, int bookId){
        Member member = members.get(memberId);
        Book book = books.get(bookId);
        if(member != null && book != null && member.getBorrowedBooks().remove(book)){
            book.setAvailable(true);
        }
    }

    private void saveBooks() throws IOException{
        Files.createDirectories(booksFile.getParent());
        List<String> list =  books.values().stream()
                .map(b -> String.join(" " ,String.valueOf(b.getId()),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getPublisher(),
                        String.valueOf(b.getCopyrightDate()))).collect(Collectors.toList());

        Files.write(booksFile, list);
    }

    private void saveMembers() throws IOException{
        Files.createDirectories(membersFile.getParent());
        List<String> list =  members.values().stream()
                .map(m -> String.join(" " ,String.valueOf(m.getId()),
                        m.getName(),
                        m.getEmail())).collect(Collectors.toList());

        Files.write(membersFile, list);
    }

    private void saveCheckOuts() throws IOException{
        Files.createDirectories(checkoutFile.getParent());
        List<String> list =  members.values().stream()
                .flatMap(m -> m.getBorrowedBooks().stream()
                        .map(b -> "Member ID " + "[" + m.getId() + "]" + " - Book/s: " + "[" + b.getId() + "]" + " " + b.getTitle()))
                        .toList();

        Files.write(checkoutFile, list);
    }

    private void loadBooks () throws IOException {
        try{
            System.out.println("-------BOOKS IN THE LIBRARY-------");
            Files.lines(booksFile).forEach(System.out::println);
            System.out.println();
        } catch (NoSuchFileException e){
            throw new NoSuchFileException("Books File does not exist");
        }
    }

    private void loadMembers () throws IOException {
        try {
            System.out.println("-------MEMBERS REGISTERED-------");
            Files.lines(membersFile).forEach(System.out::println);
            System.out.println();
        } catch (NoSuchFileException e){
            throw new NoSuchFileException("Members File does not exist");
        }
    }

    private void loadCheckouts () throws IOException {
        try{
            System.out.println("-------CHECKOUT BOOKS-------");
            Files.lines(checkoutFile).forEach(System.out::println);
            System.out.println();
        } catch (NoSuchFileException e){
            throw new NoSuchFileException("Checkout File does not exist");
        }
    }

    public void save() throws IOException {
        saveBooks();
        saveMembers();
        saveCheckOuts();
    }

    public void load() throws IOException {
        loadBooks();
        loadMembers();
        loadCheckouts();
    }
}
