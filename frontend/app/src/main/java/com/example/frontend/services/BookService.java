package com.example.frontend.services;

import com.example.frontend.networks.UnsafeOkHttpClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.PaginationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService {
    @GET("book/search")
    Call<PaginationResponse> searchBooksByName(@Query("name") String name,
                                               @Query("page") int page,
                                               @Query("size") int size);
        @GET("book/trending")
    Call<BookResponse> getBooksTrending();
        @GET("book/user/{id}")
    Call<BookResponse> getBooksByUserId(@Path("id") Long id);
        @GET("book/new")
    Call<BookResponse> getBooksNew();

    @GET("book/{id}")
    Call<BookResponse> getBookById(@Path("id") Long id);
}
