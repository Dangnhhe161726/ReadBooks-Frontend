package com.example.frontend.networks;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    public static Retrofit getClient(Context context) {
        if (client == null) {
            try {
                client = new OkHttpClient.Builder()
                        .addInterceptor(new AuthInterceptor(context))
                        .build();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }

        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
//            Use with url not safe
//            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/v1/")
                    .client(client)
//                  .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
