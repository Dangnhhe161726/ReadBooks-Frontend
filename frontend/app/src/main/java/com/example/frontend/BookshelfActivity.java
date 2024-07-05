package com.example.frontend;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookshelfActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BookshelfAdapter adapter;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_screen);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new BookshelfAdapter(bookList);
        recyclerView.setAdapter(adapter);
    }
}
