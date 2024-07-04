package com.example.frontend.models;

import java.util.List;

public class Data {
    private List<Category> Category;

    private List<Book> Books;

    public List<Book> getBooks() {
        return Books;
    }

    public void setBooks(List<Book> books) {
        Books = books;
    }

    public List<com.example.frontend.models.Category> getCategory() {
        return Category;
    }

    public void setCategory(List<com.example.frontend.models.Category> category) {
        Category = category;
    }
}
