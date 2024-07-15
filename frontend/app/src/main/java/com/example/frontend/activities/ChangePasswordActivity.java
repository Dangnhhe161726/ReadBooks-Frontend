package com.example.frontend.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;
import com.example.frontend.requests.ChangePasswordRequest;
import com.example.frontend.responses.ChangePasswordResponse;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.services.AuthorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button btSave, btBack;
    private EditText edCurPass, edNewPass, edConPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng ChangePasswordActivity
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });

        // Lắng nghe sự thay đổi trên edConPass để kiểm tra Confirm Password
        edConPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = edNewPass.getText().toString().trim();
                String confirmPassword = s.toString().trim();

                if (!newPassword.equals(confirmPassword)) {
                    edConPass.setError("Confirm Password does not match New Password");
                } else {
                    edConPass.setError(null);
                }
            }
        });
    }

    private void initView() {
        btSave = findViewById(R.id.btnChangePassword);
        btBack = findViewById(R.id.btnBack);
        edCurPass = findViewById(R.id.edtCurrentPassword);
        edNewPass = findViewById(R.id.edtNewPassword);
        edConPass = findViewById(R.id.edtConfirmPassword);
    }

    private void handleChangePassword() {
        String currentPassword = edCurPass.getText().toString().trim();
        String newPassword = edNewPass.getText().toString().trim();
        String confirmPassword = edConPass.getText().toString().trim();

        // Validate new password
        if (!isValidNewPassword(newPassword)) {
            return;
        }

        // Check if new password matches confirm password
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);

        // Sử dụng RetrofitClient đã được cập nhật để tạo instance của AuthorService
        Context context = ChangePasswordActivity.this;
        Retrofit retrofit = RetrofitClient.getClient(context);
        AuthorService authorService = retrofit.create(AuthorService.class);
        Call<ChangePasswordResponse> call = authorService.changePassword(request);

        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Quay trở lại màn hình trước
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidNewPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "New Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 4) {
            Toast.makeText(this, "New Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "New Password must contain at least one digit", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
