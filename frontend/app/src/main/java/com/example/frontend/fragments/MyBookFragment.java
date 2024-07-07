package com.example.frontend.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.models.Book;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.services.BookService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookFragment extends Fragment {
    BookshelfAdapter adapter;
    private RecyclerView recyclerView;
    private BookshelfAdapter bookAdapter;
    private BookService bookService;
    List<Book> bookList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookshelf_screen, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookList = new ArrayList<>();
        bookAdapter = new BookshelfAdapter(getContext(), bookList);
        recyclerView.setAdapter(bookAdapter);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);
        fetchBooks(1L);
        return view;
    }

    private void fetchBooks(Long id) {
        Call<BookResponse> call = bookService.getBooksByUserId(id);
        call.enqueue(new Callback<BookResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body().getData().getBooks();
                    bookList.clear();
                    bookList.addAll(books);
                    bookAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                    Log.e("API Error", "Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Log.e("API Error", "Request Failed: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
