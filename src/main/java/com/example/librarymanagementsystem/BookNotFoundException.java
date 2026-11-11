package com.example.librarymanagementsystem;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(String msg){
        super(msg);
    }
}
