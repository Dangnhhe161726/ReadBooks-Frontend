package com.example.frontend.services;

import com.example.frontend.networks.UnsafeOkHttpClient;
import com.example.frontend.requests.LoginRequest;
import com.example.frontend.requests.RegisterRequest;
import com.example.frontend.responses.LoginResponse;
import com.example.frontend.responses.RegisterResponse;
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
