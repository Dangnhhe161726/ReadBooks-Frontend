package com.example.frontend.service;

import com.example.frontend.network.UnsafeOkHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface BookService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    BookService bookService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(BookService.class);


}
