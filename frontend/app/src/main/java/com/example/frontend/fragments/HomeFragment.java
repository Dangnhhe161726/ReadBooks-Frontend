package com.example.frontend.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.frontend.R;
import com.example.frontend.models.Category;
import com.example.frontend.network.RetrofitClient;
import com.example.frontend.response.CategoryResponse;
import com.example.frontend.service.CategoryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private LinearLayout categoryContainer;
    private CategoryService categoryService;
    private String authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vbnNzMDkxMEBnbWFpbC5jb20iLCJpYXQiOjE3MjAwNTkzODAsImV4cCI6MTcyMDEwOTQ0NX0.cj0S6oOLTziMOtCAS1XEmWfODIEE8omttW0Q1cJAtOIyywrD2xQXWMPYn5sha8cBL9pOs4UYrSvpRpw32kr_Uw"; // You should get this token after user logs in

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page, container, false);
        categoryContainer = view.findViewById(R.id.categoryContainer);
        categoryService = RetrofitClient.getClient(authToken).create(CategoryService.class);
        fetchCategories();
        return view;

    }

    private void fetchCategories() {
        Call<CategoryResponse> call = categoryService.getCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData().getCategory();
                    displayCategories(categories);
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        for (Category category : categories) {
            // Create a new TextView for each category
            TextView categoryTextView = new TextView(getContext());
            categoryTextView.setText(category.getName());
            categoryTextView.setTextSize(16); // Set text size as needed
            categoryTextView.setPadding(8, 8, 8, 8); // Set padding as needed
            categoryTextView.setBackgroundResource(R.drawable.caterogy_background); // Set background with border

            // Add margin to each TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0); // Add margin between categories
            categoryTextView.setLayoutParams(params);

            // Add the TextView to the LinearLayout
            categoryContainer.addView(categoryTextView);
        }
    }
}

