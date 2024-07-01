package com.example.frontend.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.frontend.MainActivity;
import com.example.frontend.OnSwipeTouchListener;
import com.example.frontend.R;
import com.example.frontend.request.LoginRequest;
import com.example.frontend.response.LoginResponse;
import com.example.frontend.service.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private int count = 0;
    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginSignIn, btnLoginSignUp;
    private TextView tvLoginForgetPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            String mess = intent.getStringExtra("mess");
            if (email != null) {
                edtLoginEmail.setText(email);
            }
            if (mess != null) {
                Toast.makeText(LoginActivity.this, mess, Toast.LENGTH_LONG).show();
            }
        }

        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                changeImageAndText();
            }

            public void onSwipeRight() {
                changeImageAndText();
            }

            public void onSwipeLeft() {
                changeImageAndText();
            }

            public void onSwipeBottom() {
                changeImageAndText();
            }
        });

        btnLoginSignIn.setOnClickListener(v -> SignIn());

        btnLoginSignUp.setOnClickListener(v -> {
            Intent loginIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            loginIntent.putExtra("loginEmail", edtLoginEmail.getText().toString());
            startActivity(loginIntent);
        });
    }

    private void changeImageAndText() {
        if (count == 0) {
            imageView.setImageResource(R.drawable.good_night_img);
            textView.setText("Night");
            count = 1;
        } else {
            imageView.setImageResource(R.drawable.good_morning_img);
            textView.setText("Morning");
            count = 0;
        }
    }

    private void SignIn() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(edtLoginEmail.getText().toString());
        loginRequest.setPassword(edtLoginPassword.getText().toString());
        UserService.userService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                saveToken(loginResponse.getData().getToken());
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                responseDataFail(throwable, "Login fail");
            }
        });
    }

    private void saveToken(String token) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "MyPrefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", token);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getToken() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "MyPrefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                // Sử dụng token để gọi API hoặc thực hiện các thao tác khác
            } else {
                // Xử lý khi không có token được lưu trữ
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteToken() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "MyPrefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void responseDataFail(Throwable throwable, String mess) {
        Log.e("API Error", "onFailure: ", throwable);
        Toast toast = Toast.makeText(LoginActivity.this, mess, Toast.LENGTH_LONG);
        toast.show();
        new Handler().postDelayed(toast::cancel, 60000);
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.editLoginPassword);
        btnLoginSignIn = findViewById(R.id.btnLoginSignIn);
        btnLoginSignUp = findViewById(R.id.btnLoginSignUp);
        tvLoginForgetPassword = findViewById(R.id.tvLoginForgetPassword);
    }
}
