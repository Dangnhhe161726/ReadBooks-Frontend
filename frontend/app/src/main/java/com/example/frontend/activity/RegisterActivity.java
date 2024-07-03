package com.example.frontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.OnSwipeTouchListener;
import com.example.frontend.R;
import com.example.frontend.request.RegisterRequest;
import com.example.frontend.response.RegisterResponse;
import com.example.frontend.response.UserResponse;
import com.example.frontend.service.UserService;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView, tvRegisterMess;
    private EditText edtRegisterFullName, edtRegisterEmail, edtRegisterPhone, edtRegisterPass, edtRegisterRePass;
    private Button btnSignIn, btnSignUp;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_sreen);
        initView();

        setupSwipeListener();
        setupButtonListeners();
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
        btnSignUp.setOnClickListener(v -> {
            try {
                signUp();
            } catch (Exception ex) {
                tvRegisterMess.setText(ex.toString());
            }
        });

        btnSignIn.setOnClickListener(v -> {
            Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(registerIntent);
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

    private void signUp() throws Exception {
        validateInputs();

        RegisterRequest registerRequest = createRegisterRequest();
        UserService.userService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                handleRegisterResponse(response);
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                responseDataFail(throwable, "Register failed");
            }
        });
    }

    private void validateInputs() throws Exception {
        if (edtRegisterFullName.getText().toString().isEmpty()) throw new Exception("Full name cannot be empty");
        if (edtRegisterEmail.getText().toString().isEmpty()) throw new Exception("Email cannot be empty");
        if (edtRegisterPhone.getText().toString().isEmpty()) throw new Exception("Phone cannot be empty");
        if (edtRegisterPass.getText().toString().isEmpty()) throw new Exception("Password cannot be empty");
        if (edtRegisterRePass.getText().toString().isEmpty()) throw new Exception("Repassword cannot be empty");
        if (!edtRegisterPass.getText().toString().equals(edtRegisterRePass.getText().toString()))
            throw new Exception("Password and repassword do not match");
    }

    private RegisterRequest createRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName(edtRegisterFullName.getText().toString());
        registerRequest.setEmail(edtRegisterEmail.getText().toString());
        registerRequest.setPhoneNumber(edtRegisterPhone.getText().toString());
        registerRequest.setPassword(edtRegisterPass.getText().toString());
        registerRequest.setRepassword(edtRegisterRePass.getText().toString());
        registerRequest.setAddress("abc");
        registerRequest.setGender(true);
        registerRequest.setRoles(Collections.singletonList(1L));
        return registerRequest;
    }

    private void handleRegisterResponse(Response<RegisterResponse> response) {
        RegisterResponse registerResponse = response.body();
        if (registerResponse != null) {
            if (registerResponse.getStatusCode() == 201) {
                UserResponse user = registerResponse.getData().getUser();
                if (user != null) {
                    Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    registerIntent.putExtra("email", user.getEmail());
                    registerIntent.putExtra("mess", "Please verify account in your email");
                    startActivity(registerIntent);
                }
            } else {
                tvRegisterMess.setText(registerResponse.getMessage());
                Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_LONG).show();
        }
    }

    private void responseDataFail(Throwable throwable, String mess) {
        Log.e("API Error", "onFailure: ", throwable);
        Toast toast = Toast.makeText(RegisterActivity.this, mess, Toast.LENGTH_LONG);
        toast.show();
        new Handler().postDelayed(toast::cancel, 60000);
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        edtRegisterFullName = findViewById(R.id.edtRegisterFullName);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPhone = findViewById(R.id.edtRegisterPhone);
        edtRegisterPass = findViewById(R.id.edtRegisterPass);
        edtRegisterRePass = findViewById(R.id.edtRegisterRePass);
        btnSignIn = findViewById(R.id.btnRegisterSignIn);
        btnSignUp = findViewById(R.id.btnRegisterSignUp);
        tvRegisterMess = findViewById(R.id.tvRegisterMess);
    }
}
