package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.CategoryDetailActivity;
import com.example.frontend.adapters.CategoryAdapter;
import com.example.frontend.fragments.searchscreen.CategoryFragment;
import com.example.frontend.models.Category;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.CategoryResponse;
import com.example.frontend.services.CategoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private CategoryService categoryService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categories = new ArrayList<>();
        adapter = new CategoryAdapter(categories,this);
        recyclerView.setAdapter(adapter);

        categoryService = RetrofitClient.getClient(view.getContext()).create(CategoryService.class);
        fetchCategories();

        return view;
    }

    private void fetchCategories() {
        Call<CategoryResponse> call = categoryService.getCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body().getData().getCategory());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
        intent.putExtra("category_id", category.getId());
        intent.putExtra("category_name", category.getName());
        startActivity(intent);
    }
}
