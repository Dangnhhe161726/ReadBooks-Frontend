package com.example.frontend.services;

import com.example.frontend.requests.BookMarkRequest;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.DataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookMarkService {

    @GET("bookmark/book/{bookId}")
    Call<DataResponse> getBookById(@Path("bookId") Long bookId);

    @POST("bookmark/create")
    Call<DataResponse> create(@Body BookMarkRequest bookMarkRequest);

    @DELETE("bookmark/delete/{id}")
    Call<DataResponse> deleteById(@Path("id") Long bookId);
}
