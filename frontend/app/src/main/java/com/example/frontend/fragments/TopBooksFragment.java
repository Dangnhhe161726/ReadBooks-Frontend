package com.example.frontend.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.BookDetailActivity;
import com.example.frontend.adapters.NewBookAdapter;
import com.example.frontend.event.OnBookClickListener;
import com.example.frontend.models.Book;
import com.example.frontend.models.UserByToken;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.services.BookService;
import com.example.frontend.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopBooksFragment extends Fragment {

    NewBooksFragment adapter;
    private RecyclerView recyclerView;
    private NewBookAdapter bookAdapter;
    private BookService bookService;
    private UserService userService;
    List<Book> bookList;
    List<Book> newBooks;
    private UserByToken user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topbooks, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        newBooks = new ArrayList<>();
        bookAdapter = new NewBookAdapter(getContext(), newBooks, new OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(bookAdapter);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);
        fetchNewBooks();
        return view;
    }

    private void fetchNewBooks() {
        // Make an API call to fetch new books and update the adapter
        Call<BookResponse> call = bookService.getBooksTrending();
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getData().getProductPage().getContent();
                    newBooks.clear();
                    newBooks.addAll(books);
                    bookAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
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
