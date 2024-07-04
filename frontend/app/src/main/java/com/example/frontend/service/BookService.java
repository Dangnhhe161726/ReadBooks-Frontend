package com.example.frontend.service;

import com.example.frontend.models.Book;
import com.example.frontend.response.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookService {
    @GET("book/user/{id}")
    Call<BookResponse> getBooksByUserId(@Path("id") Long id);
}
