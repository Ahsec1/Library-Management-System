package com.example.librarymanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Member {

    private int id;
    private String name;
    private String email;
    private List<Book> borrowedBooks = new ArrayList<>();

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n" + id + " | " + name + " | " + email + "\nBorrowed Books: " + borrowedBooks;
    }
}
