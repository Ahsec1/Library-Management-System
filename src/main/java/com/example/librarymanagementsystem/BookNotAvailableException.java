package com.example.librarymanagementsystem;

public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String msg){
        super(msg);
    }
}
