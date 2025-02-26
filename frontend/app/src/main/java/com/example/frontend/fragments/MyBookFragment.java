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
import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.adapters.TopTrendBookAdapter;
import com.example.frontend.event.OnBookClickListener;
import com.example.frontend.models.Book;
import com.example.frontend.models.UserByToken;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.services.BookService;
import com.example.frontend.services.UserService;

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
    private UserService userService;
    List<Book> bookList;
    private UserByToken user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookshelf_screen, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookList = new ArrayList<>();
        bookAdapter = new BookshelfAdapter(getContext(), bookList, new OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                startActivity(intent);
            }
        });        recyclerView.setAdapter(bookAdapter);
        userService = RetrofitClient.getClient(view.getContext()).create(UserService.class);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);
        getUser();
        return view;
    }

    private void getUser() {
        Call<DataResponse> call = userService.getUserInfor();

        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    user = response.body().getData().getUserByToken();
                    if (user != null) {
                        fetchBooks(user.getId());
                    } else {
                        Log.e("API Error", "User is null");
                    }
                } else {
                    Log.e("API Error", "Failed to get user info: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
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
