package com.example.frontend.activity;

import android.annotation.SuppressLint;
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
    private TextView textView, tvLoginMess;
    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginSignIn, btnLoginSignUp;
    private TextView tvLoginForgetPassword;
    private int count = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_screen);

        initView();
        handleIntent();
        setupSwipeListener();
        setupButtonListeners();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            String mess = intent.getStringExtra("mess");
            if (email != null) {
                edtLoginEmail.setText(email);
                tvLoginMess.setText(mess);
            }
            if (mess != null) {
                Toast.makeText(LoginActivity.this, mess, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupSwipeListener() {
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeTop() {
                changeImageAndText();
            }

            @Override
            public void onSwipeRight() {
                changeImageAndText();
            }

            @Override
            public void onSwipeLeft() {
                changeImageAndText();
            }

            @Override
            public void onSwipeBottom() {
                changeImageAndText();
            }
        });
    }

    private void setupButtonListeners() {
        btnLoginSignIn.setOnClickListener(v -> signIn());

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

    private void signIn() {
        try {
            validateInputs();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(edtLoginEmail.getText().toString());
            loginRequest.setPassword(edtLoginPassword.getText().toString());
            UserService.userService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    handleLoginResponse(response);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    responseDataFail(throwable, "Login failed");
                }
            });
        } catch (Exception ex) {
            tvLoginMess.setText(ex.toString());
        }
    }

    private void validateInputs() throws Exception {
        if (edtLoginEmail.getText().toString().isEmpty())
            throw new Exception("Email is not empty");
        if (edtLoginPassword.getText().toString().isEmpty())
            throw new Exception("Password is not empty");
    }

    private void handleLoginResponse(Response<LoginResponse> response) {
        if (response == null || response.body() == null) {
            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        } else {
            LoginResponse loginResponse = response.body();
            if (loginResponse.getStatusCode() == 200) {
                saveToken(loginResponse.getData().getToken());
                Intent loginIntent = new Intent(LoginActivity.this, SearchActivity.class);
                startActivity(loginIntent);
            } else {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
            }
        }
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
        tvLoginMess = findViewById(R.id.tvLoginMess);
    }
}
