package com.example.frontend.service;

import com.example.frontend.network.UnsafeOkHttpClient;
import com.example.frontend.request.LoginRequest;
import com.example.frontend.request.RegisterRequest;
import com.example.frontend.response.LoginResponse;
import com.example.frontend.response.RegisterResponse;
import com.example.frontend.response.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    UserService userService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(UserService.class);


    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
