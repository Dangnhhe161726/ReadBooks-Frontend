package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapters.CategoryAdapter;
import com.example.frontend.fragments.bookdetail.BookIntroFragment;
import com.example.frontend.fragments.bookdetail.CommentsFragment;
import com.example.frontend.fragments.bookdetail.RelatedBooksFragment;
import com.example.frontend.models.Book;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.services.BookService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private Button btnReadBook;
    private ImageView ivThumbnail;
    private TextView tvBookTitle, tvBookAuthor, tvFavorites, tvViewer;
    private RecyclerView rvCategories;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    private BookIntroFragment bookIntroFragment;
    private CommentsFragment commentsFragment;
    private RelatedBooksFragment relatedBooksFragment;
    private String bookFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);

        initView();

        btnReadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndOpenPDF();
            }
        });

        // Get book details from API
        long bookId = 1; // Example book ID
        fetchBookDetails(bookId);

        // Initialize fragments
        bookIntroFragment = new BookIntroFragment();
        commentsFragment = new CommentsFragment();
        relatedBooksFragment = new RelatedBooksFragment();

        // Set the initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, bookIntroFragment)
                .commit();

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_intro) {
                    selectedFragment = bookIntroFragment;
                } else if (item.getItemId() == R.id.navigation_comments) {
                    selectedFragment = commentsFragment;
                } else if (item.getItemId() == R.id.navigation_related_books) {
                    selectedFragment = relatedBooksFragment;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }

    private void initView() {
        btnReadBook = findViewById(R.id.btnReadBook);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvBookAuthor = findViewById(R.id.tvBookAuthor);
        tvFavorites = findViewById(R.id.tvFavorites);
        tvViewer = findViewById(R.id.tvViewer);
        rvCategories = findViewById(R.id.rvCategories);

        // Initialize views
        bottomNavigationView = findViewById(R.id.menuBookDetail);
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    private void fetchBookDetails(long bookId) {
        BookService apiService = RetrofitClient.getClient(this).create(BookService.class);
        Call<BookResponse> call = apiService.getBookDetails(bookId);

        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body().getData().getBook();

                    // Populate UI with book details
                    populateBookDetails(book);

                    // Set introduce text in BookIntroFragment
                    if (bookIntroFragment != null) {
                        bookIntroFragment.setIntroduceText(book.getIntroduce());
                    }

                    // Set book file name
                    bookFileName = book.getLink();
                } else {
                    Toast.makeText(BookDetailActivity.this, "Failed to fetch book details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "Network error! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateBookDetails(Book book) {
        // Populate UI components with book details
        Glide.with(this).load(book.getThumbnail()).into(ivThumbnail);

        // Populate UI elements with book details
        tvBookTitle.setText(book.getName());
        tvBookAuthor.setText(book.getAuthor().getName());
        tvFavorites.setText(book.getFavorites() + " Favorites");
        tvViewer.setText(book.getView() + " Views");

        // Populate categories in RecyclerView
        CategoryAdapter adapter = new CategoryAdapter(book.getCategories());
        rvCategories.setAdapter(adapter);
    }

    private void downloadAndOpenPDF() {
        if (bookFileName == null || bookFileName.isEmpty()) {
            Toast.makeText(this, "No book file available", Toast.LENGTH_SHORT).show();
            return;
        }

        String downloadUrl = "{{API_PREFIX}}/images/download-from-amazon?fileName=" + bookFileName;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), bookFileName);

                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }

                    outputStream.close();
                    inputStream.close();

                    Intent intent = new Intent(BookDetailActivity.this, ReadBookActivity.class);
                    intent.putExtra("filePath", file.getAbsolutePath());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookDetailActivity.this, "Failed to download book", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}