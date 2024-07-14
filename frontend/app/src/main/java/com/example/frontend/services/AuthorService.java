package com.example.frontend.services;

import com.example.frontend.models.Data;
import com.example.frontend.networks.UnsafeOkHttpClient;
import com.example.frontend.requests.ChangePasswordRequest;
import com.example.frontend.requests.UpdateProfileRequest;
import com.example.frontend.responses.ChangePasswordResponse;
import com.example.frontend.responses.ProfileResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthorService {
    @GET("auth/profile")
    Call<ProfileResponse> getProfile();

    @POST("auth/change-password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("auth/update-profile")
    Call<ProfileResponse> updateProfile(@Body UpdateProfileRequest request);
}
