package com.example.frontend.fragments.searchscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.CategoryDetailActivity;
import com.example.frontend.adapters.searchscreen.SearchCategoryAdapter;
import com.example.frontend.event.OnCategoryClickListener;
import com.example.frontend.models.Category;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.GeneralPaginationResponses;
import com.example.frontend.services.CategoryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements OnCategoryClickListener {

    private RecyclerView recyclerViewCategory;
    private SearchCategoryAdapter adapter;
    private String query;
    private int currentPage;
    private int pageSize;

    private CategoryService categoryService;

    public void searchCategory(String query) {
        Toast.makeText(getContext(), "Searching categories: " + query, Toast.LENGTH_SHORT).show();
        resetSearch();
        searchCategoriesByName(query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_search_screen, container, false);
        categoryService = RetrofitClient.getClient(view.getContext()).create(CategoryService.class);
        recyclerViewCategory = view.findViewById(R.id.reViewFragmentCategory);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(manager);
        adapter = new SearchCategoryAdapter(this); // Pass listener to adapter
        recyclerViewCategory.setAdapter(adapter);
        currentPage = 0;
        pageSize = 5;
        return view;
    }

    private void resetSearch() {
        currentPage = 0;
        adapter.clearCategories();
        recyclerViewCategory.scrollToPosition(0);
        recyclerViewCategory.clearOnScrollListeners();
    }

    private void searchCategoriesByName(String query) {
        Call<GeneralPaginationResponses<Category>> responseCall = categoryService.searchCategoriesByName(query, currentPage, pageSize);
        responseCall.enqueue(new Callback<GeneralPaginationResponses<Category>>() {
            @Override
            public void onResponse(Call<GeneralPaginationResponses<Category>> call, Response<GeneralPaginationResponses<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeneralPaginationResponses<Category> paginationResponse = response.body();
                    if (paginationResponse.getContent().size() != 0) {
                        adapter.addCategories(paginationResponse.getContent());
                        currentPage++;
                        recyclerViewCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (!recyclerViewCategory.canScrollVertically(1)) {
                                    searchCategoriesByName(query);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralPaginationResponses<Category>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_LONG).show();
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
