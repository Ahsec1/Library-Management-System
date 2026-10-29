package com.example.librarymanagementsystem;

public class Book {

    private int id;
    private String title;
    private String author;
    private String publisher;
    private String copyrightDate;
    private Boolean available = true;

    public Book(int id, String title, String author, String publisher, String copyrightDate){
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.copyrightDate = copyrightDate;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCopyrightDate() {
        return copyrightDate;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available){
        this.available = available;
    }

    @Override
    public String toString() {
        return "\n" + id + " | " + title + " by " + author + " | " + publisher + ", " + copyrightDate;
    }
}
