package com.example.frontend.models;

import com.example.frontend.responses.UserResponse;

import java.util.List;

public class Data {
    private List<Category> Category;
    private List<Book> Books;
    private String Token;
    private UserResponse user;

    public Data() {
    }

    public List<Book> getBooks() {
        return Books;
    }

    public void setBooks(List<Book> books) {
        Books = books;
    }

    public List<Category> getCategory() {
        return Category;
    }

    public void setCategory(List<Category> category) {
        Category = category;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
