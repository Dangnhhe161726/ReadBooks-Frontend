package com.example.frontend.fragments;

import static com.example.frontend.R.id.recyclerViewTrendingBooks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.BookDetailActivity;
import com.example.frontend.adapters.BookshelfAdapter;
import com.example.frontend.adapters.TopTrendBookAdapter;
import com.example.frontend.event.OnBookClickListener;
import com.example.frontend.models.Author;
import com.example.frontend.models.Book;
import com.example.frontend.models.Category;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.AuthorResponse;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.CategoryResponse;
import com.example.frontend.services.AuthorService;
import com.example.frontend.services.BookService;
import com.example.frontend.services.CategoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private LinearLayout categoryContainer;
    private LinearLayout authorContainer;
    private CategoryService categoryService;
    private AuthorService authorService;
    private RecyclerView recyclerViewTrendingBooks;
    private RecyclerView recyclerViewNewBook;

    List<Book> bookListTrending;
    List<Book> newBooks;
    private TopTrendBookAdapter bookAdapter;
    private TopTrendBookAdapter newBookAdapter;
    private BookService bookService;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page, container, false);
        authorContainer = view.findViewById(R.id.authorContainer);
        categoryContainer = view.findViewById(R.id.categoryContainer);
        categoryService = RetrofitClient.getClient(view.getContext()).create(CategoryService.class);
        authorService = RetrofitClient.getClient(view.getContext()).create(AuthorService.class);
        recyclerViewTrendingBooks = view.findViewById(R.id.recyclerViewTrendingBooks);
        recyclerViewNewBook = view.findViewById(R.id.recyclerViewNewBooks);
        recyclerViewTrendingBooks.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewBook.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        bookListTrending = new ArrayList<>();
        newBooks = new ArrayList<>();
        bookAdapter = new TopTrendBookAdapter(getContext(), bookListTrending, new OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                startActivity(intent);
            }
        });
        newBookAdapter = new TopTrendBookAdapter(getContext(), newBooks, new OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                startActivity(intent);
            }
        });
        recyclerViewTrendingBooks.setAdapter(bookAdapter);
        recyclerViewNewBook.setAdapter(newBookAdapter);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);

        fetchAuthors();
        fetchCategories();
        fetchTrendingBooks();
        fetchNewBooks();
        return view;

    }
    private void fetchNewBooks() {
        // Make an API call to fetch new books and update the adapter
        Call<BookResponse> call = bookService.getBooksNew();
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getData().getProductPage().getContent();
                    newBooks.clear();
                    newBooks.addAll(books);
                    newBookAdapter.notifyDataSetChanged();
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
    private void fetchTrendingBooks() {
        Call<BookResponse> call = bookService.getBooksTrending();
        call.enqueue(new Callback<BookResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body().getData().getProductPage().getContent();
                    bookListTrending.clear();
                    bookListTrending.addAll(books);
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
    private void fetchCategories() {
        Call<CategoryResponse> call = categoryService.getCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData().getCategory();
                    displayCategories(categories);
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }
    private void fetchAuthors() {
        Call<AuthorResponse> call = authorService.getAuthors();
        call.enqueue(new Callback<AuthorResponse>() {
            @Override
            public void onResponse(Call<AuthorResponse> call, Response<AuthorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Author> categories = response.body().getData().getAuthors();
                    displayAuthors(categories);
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthorResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        for (Category category : categories) {
            // Create a new TextView for each category
            TextView categoryTextView = new TextView(getContext());
            categoryTextView.setText(category.getName());
            categoryTextView.setTextSize(16); // Set text size as needed
            categoryTextView.setPadding(8, 8, 8, 8); // Set padding as needed
            categoryTextView.setBackgroundResource(R.drawable.caterogy_background); // Set background with border

            // Add margin to each TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0); // Add margin between categories
            categoryTextView.setLayoutParams(params);

            // Add the TextView to the LinearLayout
            categoryContainer.addView(categoryTextView);
        }
    }   private void displayAuthors(List<Author> authors) {
        if (authors == null) {
            Log.e("Display Error", "Authors list is null");
            return;
        }

        for (Author author : authors) {
            // Create a new TextView for each author
            TextView authorTextView = new TextView(getContext());
            authorTextView.setText(author.getName());
            authorTextView.setTextSize(16); // Set text size as needed
            authorTextView.setPadding(8, 8, 8, 8); // Set padding as needed
            authorTextView.setBackgroundResource(R.drawable.caterogy_background); // Set background with border

            // Add margin to each TextView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0); // Add margin between authors
            authorTextView.setLayoutParams(params);

            // Add the TextView to the LinearLayout
            authorContainer.addView(authorTextView);
        }
    }
}

