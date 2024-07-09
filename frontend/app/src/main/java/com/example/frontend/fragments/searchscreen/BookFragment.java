package com.example.frontend.fragments.searchscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapters.searchscreen.SearchBookAdapter;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.PaginationResponse;
import com.example.frontend.services.BookService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {

    private RecyclerView reViewFragmentBook;
    private SearchBookAdapter adapter;
    private String query;
    private int currentPage;
    private int pageSize;

    private BookService bookService;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search_screen, container, false);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);
        reViewFragmentBook = view.findViewById(R.id.reViewFragmentBook);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        reViewFragmentBook.setLayoutManager(manager);
        adapter = new SearchBookAdapter();
        reViewFragmentBook.setAdapter(adapter);
        currentPage = 0;
        pageSize = 1;
        reViewFragmentBook.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!reViewFragmentBook.canScrollVertically(1)) {
                    searchBooks(query);
                }
            }
        });
        return view;
    }

    public void searchBooks(String query) {
        Call<PaginationResponse> responseCall = bookService.searchBooksByName(query, currentPage, pageSize);
        responseCall.enqueue(new Callback<PaginationResponse>() {
            @Override
            public void onResponse(Call<PaginationResponse> call, Response<PaginationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaginationResponse paginationResponse = response.body();
                    adapter.addBooks(paginationResponse.getContent());
                    currentPage++;
                }
            }

            @Override
            public void onFailure(Call<PaginationResponse> call, Throwable throwable) {
                int a = 0;
            }
        });

//        BookService.bookService.searchBooksByName(query, currentPage, pageSize).enqueue(new Callback<PaginationResponse>() {
//            @Override
//            public void onResponse(Call<PaginationResponse> call, Response<PaginationResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    PaginationResponse paginationResponse = response.body();
//                    adapter.addBooks(paginationResponse.getContent());
//                    currentPage++;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PaginationResponse> call, Throwable throwable) {
//
//            }
//        });
    }
}
