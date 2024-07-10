package com.example.frontend.services;

import com.example.frontend.networks.UnsafeOkHttpClient;
import com.example.frontend.responses.AuthorResponse;
import com.example.frontend.responses.CategoryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface AuthorService {
    @GET("author")
    Call<AuthorResponse> getAuthors();
}
