package com.example.frontend.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapters.NewBookAdapter;
import com.example.frontend.event.OnBookClickListener;
import com.example.frontend.models.Book;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.services.BookService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAuthorActivity extends AppCompatActivity {
    private BookService bookService;
    List<Book> bookList;
    private NewBookAdapter bookAdapter;
    private RecyclerView recyclerView;
    private Long authorId;
    private String authorName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        authorId = getIntent().getLongExtra("author_id", -1);
        authorName = getIntent().getStringExtra("author_name");

        if (authorId == -1 || authorName == null) {
            Toast.makeText(this, "Invalid author information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView authorNameTextView = findViewById(R.id.tvIntroduction);

        authorNameTextView.setText("Tác giả: "+authorName);
        ImageButton backButton = findViewById(R.id.btn_back_cate);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = this.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookList = new ArrayList<>();
        bookAdapter = new NewBookAdapter(this, bookList, new OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(BookAuthorActivity.this, BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(bookAdapter);
        bookService = RetrofitClient.getClient(this).create(BookService.class);
        fetchBooks(authorId);
    }

    private void fetchBooks(Long id) {
        Call<BookResponse> call = bookService.getBooksByAuthorId(id);
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
