package com.example.frontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private EditText edtRegisterFullName, edtRegisterEmail, edtRegisterPhone, edtRegisterPass, edtRegisterRePass;
    private Button btnSignIn, btnSignUp;
    private int count = 0;
    private Intent registerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);
        initView();


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

        btnSignUp.setOnClickListener(v -> {
            SignUp();
            startActivity(registerIntent);
        });

        btnSignIn.setOnClickListener(v -> startActivity(registerIntent));
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

    private void SignUp() {
        List<Long> role = new ArrayList<>();
        role.add(1L);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName(edtRegisterFullName.getText().toString());
        registerRequest.setEmail(edtRegisterEmail.getText().toString());
        registerRequest.setPhoneNumber(edtRegisterPhone.getText().toString());
        registerRequest.setPassword(edtRegisterPass.getText().toString());
        registerRequest.setRepassword(edtRegisterRePass.getText().toString());
        registerRequest.setAddress("abc");
        registerRequest.setGender(true);
        registerRequest.setRoles(role);
        UserService.userService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if (registerResponse != null && registerResponse.getStatusCode() == 201) {
                    UserResponse user = registerResponse.getUser();
                    if (user != null) {
                        Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        registerIntent.putExtra("email", user.getEmail());
                        registerIntent.putExtra("mess", "Please verify account in your email");
                        startActivity(registerIntent);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, registerResponse != null ? registerResponse.getMessage() : "Register failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                responseDataFail(throwable, "Register fail");
            }
        });
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
        registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
    }
}
