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
import com.example.frontend.activities.BookAuthorActivity;
import com.example.frontend.activities.CategoryDetailActivity;
import com.example.frontend.adapters.searchscreen.SearchAuthorAdapter;
import com.example.frontend.event.OnAuthorClickListener;
import com.example.frontend.models.Author;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.GeneralPaginationResponses;
import com.example.frontend.services.AuthorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorFragment extends Fragment implements OnAuthorClickListener {

    private RecyclerView recyclerViewAuthor;
    private SearchAuthorAdapter adapter;
    private String query;
    private int currentPage;
    private int pageSize;

    private AuthorService authorService;

    public void searchAuthors(String query) {
        Toast.makeText(getContext(), "Searching authors: " + query, Toast.LENGTH_SHORT).show();
        resetSearch();
        searchAuthorsByName(query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author_search_screen, container, false);
        authorService = RetrofitClient.getClient(view.getContext()).create(AuthorService.class);
        recyclerViewAuthor = view.findViewById(R.id.reViewFragmentAuthor);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewAuthor.setLayoutManager(manager);
        adapter = new SearchAuthorAdapter(this); // Pass listener to adapter
        recyclerViewAuthor.setAdapter(adapter);
        currentPage = 0;
        pageSize = 5;
        return view;
    }

    private void resetSearch() {
        currentPage = 0;
        adapter.clearAuthors();
        recyclerViewAuthor.scrollToPosition(0);
        recyclerViewAuthor.clearOnScrollListeners();
    }

    private void searchAuthorsByName(String query) {
        Call<GeneralPaginationResponses<Author>> responseCall = authorService.searchAuthorsByName(query, currentPage, pageSize);
        responseCall.enqueue(new Callback<GeneralPaginationResponses<Author>>() {
            @Override
            public void onResponse(Call<GeneralPaginationResponses<Author>> call, Response<GeneralPaginationResponses<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeneralPaginationResponses<Author> paginationResponse = response.body();
                    if (paginationResponse.getContent().size() != 0) {
                        adapter.addAuthors(paginationResponse.getContent());
                        currentPage++;
                        recyclerViewAuthor.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (!recyclerView.canScrollVertically(1)) {
                                    searchAuthorsByName(query);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralPaginationResponses<Author>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to load authors", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAuthorClick(Author author) {
        Intent intent = new Intent(getActivity(), BookAuthorActivity.class);
        intent.putExtra("author_id", author.getId());
        intent.putExtra("author_name", author.getName());
        startActivity(intent);
    }
}
