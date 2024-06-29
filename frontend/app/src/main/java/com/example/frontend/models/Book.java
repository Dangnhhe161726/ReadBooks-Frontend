package com.example.frontend.models;

public class Book {
    private String title;
    private String author;
    private int coverResourceId;

    public Book(String title, String author, int coverResourceId) {
        this.title = title;
        this.author = author;
        this.coverResourceId = coverResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }
}
