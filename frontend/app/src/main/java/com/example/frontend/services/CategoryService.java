package com.example.frontend.services;

import com.example.frontend.models.Category;
import com.example.frontend.responses.CategoryResponse;
import com.example.frontend.responses.GeneralPaginationResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("category")
    Call<CategoryResponse> getCategories();

    @GET("category/search")
    Call<GeneralPaginationResponses<Category>> searchCategoriesByName(
            @Query("name") String name,
            @Query("page") int page,
            @Query("size") int size
    );
}
