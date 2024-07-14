package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frontend.R;
import com.example.frontend.adapters.menubookmark.BookmarkAdapter;
import com.example.frontend.models.BookMark;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.services.BookMarkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuBookMarkActivity extends AppCompatActivity {
    private Button btnExit;
    private TextView tvNameBook;
    private ListView lv;
    private BookmarkAdapter adapter;
    private List<BookMark> bookMarkList;
    private String bookUrl;
    private Long bookId;
    private String bookName;
    private Long userId;
    private BookMarkService bookMarkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_bookmark);
        btnExit = findViewById(R.id.btnExit);
        tvNameBook = findViewById(R.id.tvNameBook);
        lv = findViewById(R.id.lvBookMark);
        bookUrl = getIntent().getStringExtra("BOOK_URL");
        bookId = getIntent().getLongExtra("BOOK_ID", -1);
        bookName = getIntent().getStringExtra("BOOK_NAME");
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (bookUrl != null && bookId != -1 && userId != -1) {
            loadData();
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuBookMarkActivity.this, ReadFileBookActivity.class);
                    intent.putExtra("BOOK_URL", bookUrl);
                    intent.putExtra("BOOK_ID", bookId);
                    intent.putExtra("BOOK_NAME", bookName);
                    startActivity(intent);
                }
            });
        }
    }

    private void loadData() {
        if (bookName.length() > 30) {
            tvNameBook.setText(bookName.substring(0, 30) + "...");
        } else {
            tvNameBook.setText(bookName);
        }

        bookMarkService = RetrofitClient.getClient(this).create(BookMarkService.class);
        Call<DataResponse> call = bookMarkService.getBookById(bookId, userId);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookMarkList = response.body().getData().getBookMarks();
                    adapter = new BookmarkAdapter(MenuBookMarkActivity.this, bookMarkList, bookId, bookUrl, bookName);
                    lv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }
}