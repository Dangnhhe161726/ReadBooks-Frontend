package com.example.frontend.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChangeAvaActivity;
import com.example.frontend.activities.ChangePasswordActivity;
import com.example.frontend.activities.DetailedProfileActivity;
import com.example.frontend.activities.LoginActivity;
import com.example.frontend.responses.ProfileResponse;
import com.example.frontend.responses.UserResponse;
import com.example.frontend.services.AuthorService;
import com.example.frontend.networks.RetrofitClient;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileFragment extends Fragment {

    private ShapeableImageView avatarImageView;
    private TextView greetingTextView;
    private UserResponse userProfile;
    private AuthorService authorService;
    private Button viewProfileButton, logoutButton, btnChangePassword, btnContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        avatarImageView = view.findViewById(R.id.image_avatar);
        greetingTextView = view.findViewById(R.id.text_greeting);
        viewProfileButton = view.findViewById(R.id.button_view_profile);
        logoutButton = view.findViewById(R.id.button_logout);
        btnChangePassword = view.findViewById(R.id.button_change_password);
        btnContact = view.findViewById(R.id.button_contact);

        // Initialize AuthorService using RetrofitClient
        authorService = RetrofitClient.getClient(view.getContext()).create(AuthorService.class);

        // Fetch profile data
        fetchUserProfile();

        // Set button click listener to open detailed profile activity
        viewProfileButton.setOnClickListener(v -> {
            if (userProfile != null) {
                // Start DetailedProfileActivity and pass userProfile
                Intent intent = new Intent(getActivity(), DetailedProfileActivity.class);
                intent.putExtra("userProfile", userProfile);
                startActivity(intent);
                Log.d("MyProfileFragment", "User Profile: " + userProfile.toString());
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "PRM392 - SE1709 - Team 5", Toast.LENGTH_SHORT).show();
            }
        });

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfile != null) {
                    Intent intent = new Intent(getActivity(), ChangeAvaActivity.class);
                    intent.putExtra("avatarUrl", userProfile.getAvatar());
                    startActivity(intent);
                }
            }
        });


        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void initView() {
    }

    private void fetchUserProfile() {
        authorService.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userProfile = response.body().getData().getUser();
                    updateUI(userProfile);
                } else {
                    // Handle unsuccessful response
                    String errorMessage = "Failed to fetch user profile: ";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorMessage += "Empty error body";
                    }
                    Log.e("MyProfileFragment", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                // Handle failure
                Log.e("MyProfileFragment", "Error fetching user profile", t);
            }
        });
    }

    private void updateUI(UserResponse userProfile) {
        if (userProfile != null) {
            if (userProfile.getAvatar() != null) {
                Glide.with(requireContext())
                        .load(userProfile.getAvatar())
                        .into(avatarImageView);
            } else {
                // Load default avatar if userProfile.getAvatar() is null
                Glide.with(requireContext())
                        .load(R.drawable.default_avatar)
                        .into(avatarImageView);
            }

            greetingTextView.setText("Hello, " + userProfile.getFullName());
        }
    }


    private void logout() {
        // Clear token or any session data (example using SharedPreferences)
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", requireActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");  // Remove token
        editor.apply();

        // Redirect to LoginActivity or your login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
        startActivity(intent);
        requireActivity().finish(); // Finish current activity to prevent user from going back
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi lại fetchUserProfile để cập nhật dữ liệu
        fetchUserProfile();
    }
}
