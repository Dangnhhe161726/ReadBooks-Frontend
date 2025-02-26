package com.example.frontend.models;

import com.example.frontend.responses.UserResponse;

import java.util.List;

public class Data {
    private List<Category> Category;
    private List<Book> Books;
    private List<Author> Authors;
    private Book Book;
    private ProductPage productPage;
    private String Token;
    private UserResponse user;
    private String url;
    private BookMark bookMark;
    private List<BookMark> bookMarks;
    private UserByToken userByToken;

    public Data() {
    }

    public UserByToken getUserByToken() {
        return userByToken;
    }

    public void setUserByToken(UserByToken userByToken) {
        this.userByToken = userByToken;
    }

    public BookMark getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
    }

    public List<BookMark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<BookMark> bookMarks) {
        this.bookMarks = bookMarks;
    }

    public Book getBook() {
        return Book;
    }

    public void setBook(Book book) {
        this.Book = book;
    }

    public ProductPage getProductPage() {
        return productPage;
    }

    public void setProductPage(ProductPage productPage) {
        this.productPage = productPage;
    }

    public List<Author> getAuthors() {
        return Authors;
    }

    public void setAuthors(List<Author> authors) {
        this.Authors = authors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
