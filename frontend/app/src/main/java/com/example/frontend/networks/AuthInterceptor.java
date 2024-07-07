package com.example.frontend.networks;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthInterceptor implements Interceptor {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public AuthInterceptor(Context context) throws GeneralSecurityException, IOException {
        this.context = context.getApplicationContext();
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        this.sharedPreferences = EncryptedSharedPreferences.create(
                "MyPrefs",
                masterKeyAlias,
                this.context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = getToken();
        if (token != null) {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(chain.request());
    }

    private String getToken() {
        return sharedPreferences.getString("token", null);
    }
}
