package com.example.frontend.services;

import com.example.frontend.models.Author;
import com.example.frontend.requests.ChangePasswordRequest;
import com.example.frontend.requests.UpdateProfileRequest;
import com.example.frontend.responses.ChangePasswordResponse;
import com.example.frontend.responses.GeneralPaginationResponses;
import com.example.frontend.responses.PaginationResponse;
import com.example.frontend.responses.ProfileResponse;
import com.example.frontend.responses.AuthorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthorService {

    @GET("auth/profile")
    Call<ProfileResponse> getProfile();

    @POST("auth/change-password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("auth/update-profile")
    Call<ProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    @GET("author")
    Call<AuthorResponse> getAuthors();

    @GET("author/search")
    Call<GeneralPaginationResponses<Author>> searchAuthorsByName(
            @Query("name") String name,
            @Query("page") int page,
            @Query("size") int size
    );
}
