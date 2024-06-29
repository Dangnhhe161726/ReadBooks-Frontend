package com.example.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.models.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    List<Book> bookList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        BookshelfAdapter adapter;
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set up adapter and other configurations for RecyclerView here
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookList = new ArrayList<>();
        bookList.add(new Book("1Q84", "Haruki Murakami", R.drawable.i5));
        bookList.add(new Book("what i talk about when i talk about running", "Haruki Murakami", R.drawable.i1));
        bookList.add(new Book("what i talk about when i talk about running", "Haruki Murakami", R.drawable.i2));
        bookList.add(new Book("what i talk about when i talk about running", "Haruki Murakami", R.drawable.i3));
        bookList.add(new Book("what i talk about when i talk about running", "Haruki Murakami", R.drawable.i4));
        // Add more books here...

        adapter = new BookshelfAdapter(bookList);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change in search input
                return false;
            }
        });
    }
}
