package com.example.frontend.services;

import com.example.frontend.responses.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("category")
    Call<CategoryResponse> getCategories();
}
