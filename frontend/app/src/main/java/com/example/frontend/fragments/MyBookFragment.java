package com.example.frontend.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.models.Book;
import com.example.frontend.network.RetrofitClient;
import com.example.frontend.response.BookResponse;
import com.example.frontend.service.BookService;
import com.example.frontend.service.CategoryService;

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
    private String authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vbnNzMDkxMEBnbWFpbC5jb20iLCJpYXQiOjE3MjAxMDQ5MzYsImV4cCI6MTcyMDE1NTAwMn0.AtRV7-GGrHLpv4x6rjV6p-sYjfw3MqSk-Qyn6o8kEhLhMin5-1c_F8E0XcWYwqPq-JeMg2bsI4w-piNHp3VKeQ";
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

        bookService = RetrofitClient.getClient(authToken).create(BookService.class);
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
