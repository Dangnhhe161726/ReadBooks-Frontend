package com.example.frontend.service;

import com.example.frontend.models.Category;
import com.example.frontend.network.UnsafeOkHttpClient;
import com.example.frontend.response.CategoryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("category")
    Call<CategoryResponse> getCategories();
}
