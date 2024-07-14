package com.example.frontend.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.requests.UpdateProfileRequest;
import com.example.frontend.responses.ProfileResponse;
import com.example.frontend.responses.UserResponse;
import com.example.frontend.services.AuthorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, phoneNumberEditText, dobEditText, addressEditText;
    private RadioGroup genderRadioGroup;
    private Button backButton, updateButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_profile);

        initView();

        UserResponse userProfile = (UserResponse) getIntent().getSerializableExtra("userProfile");

        if (userProfile != null) {
            fullNameEditText.setText(userProfile.getFullName());
            emailEditText.setText(userProfile.getEmail());
            phoneNumberEditText.setText(userProfile.getPhoneNumber());
            addressEditText.setText(userProfile.getAddress());

            // Chuyển đổi định dạng ngày từ yyyy-MM-dd sang dd/MM/yyyy
            String formattedDob = formatDate(userProfile.getDob().toString(), "yyyy-MM-dd", "dd/MM/yyyy");
            dobEditText.setText(formattedDob);

            // Đặt giới tính từ userProfile
            if (userProfile.isGender()) {
                genderRadioGroup.check(R.id.rb_male);
            } else {
                genderRadioGroup.check(R.id.rb_female);
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // Đóng DetailedProfileActivity
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateButton.setVisibility(View.VISIBLE); // Hiển thị nút Update khi có thay đổi
            }
        };

        fullNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        phoneNumberEditText.addTextChangedListener(textWatcher);
        addressEditText.addTextChangedListener(textWatcher);

        // Thêm TextWatcher cho dobEditText để hiển thị nút Update khi có thay đổi
        dobEditText.addTextChangedListener(textWatcher);

        // Thêm sự kiện onClick cho dobEditText để mở DatePickerDialog
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        // Thêm sự kiện cho RadioGroup
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateButton.setVisibility(View.VISIBLE); // Hiển thị nút Update khi giới tính được thay đổi
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void initView() {
        fullNameEditText = findViewById(R.id.edt_full_name);
        emailEditText = findViewById(R.id.edt_email);
        phoneNumberEditText = findViewById(R.id.edt_phone_number);
        dobEditText = findViewById(R.id.edt_dob);
        addressEditText = findViewById(R.id.edt_address);
        backButton = findViewById(R.id.btn_back);
        updateButton = findViewById(R.id.btn_update);
        genderRadioGroup = findViewById(R.id.rg_gender);
    }

    private void openDatePicker() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailedProfileActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        dobEditText.setText(selectedDate);
                        updateButton.setVisibility(View.VISIBLE); // Hiển thị nút Update khi ngày sinh được thay đổi
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private String formatDate(String date, String fromFormat, String toFormat) {
        SimpleDateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toDateFormat = new SimpleDateFormat(toFormat);
        Date parsedDate;
        try {
            parsedDate = fromDateFormat.parse(date);
            return toDateFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateProfile() {
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String address = addressEditText.getText().toString();
        boolean gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.rb_male;

        // Tạo đối tượng request
        UpdateProfileRequest request = new UpdateProfileRequest(fullName, email, phoneNumber, dob, address, gender);

        // Gọi service để cập nhật hồ sơ
        AuthorService service = RetrofitClient.getClient(getApplicationContext()).create(AuthorService.class);
        Call<ProfileResponse> call = service.updateProfile(request);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    ProfileResponse updatedProfile = response.body();
                    if (updatedProfile != null) {
                        // Cập nhật giao diện với thông tin hồ sơ mới
                        // ...
                        Toast.makeText(DetailedProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        updateButton.setVisibility(View.GONE); // Ẩn nút Update sau khi cập nhật thành công
                    }
                } else {
                    Toast.makeText(DetailedProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(DetailedProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
