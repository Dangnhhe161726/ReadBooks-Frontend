package com.example.frontend.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.R;
import com.example.frontend.adapters.CommentsAdapter;
import com.example.frontend.models.Book;
import com.example.frontend.models.FeedBack;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.UrlResponse;
import com.example.frontend.services.BookService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {
    private BookService bookService;
    private boolean isExpanded = false;
    private static final int MAX_LENGTH = 100;
    private ImageView bookCover;
    private ImageView bookCoverBlur;
    private CommentsAdapter commentsAdapter;
    private List<FeedBack> commentList;
    private TextView bookTitle, bookAuthor, comments_title, bookDescription;
    private LinearLayout tagContainer;
    private RecyclerView commentRecyclerView;
    private Button btnStarReading;
    private Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookdetail_layout);
        book = new Book();
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bookCoverBlur = findViewById(R.id.blurred_background);
        bookTitle = findViewById(R.id.book_title);
        bookCover = findViewById(R.id.book_cover);
        bookAuthor = findViewById(R.id.book_author);
        commentRecyclerView = findViewById(R.id.comments_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        comments_title = findViewById(R.id.comments_title);
        bookDescription = findViewById(R.id.book_description);
        tagContainer = findViewById(R.id.tag_container);
        btnStarReading = findViewById(R.id.btn_start_reading);
        Button btnAddToShelf = findViewById(R.id.btn_add_to_shelf);

        // Retrieve the book ID from the intent
        commentList = new ArrayList<>();
        Long bookId = getIntent().getLongExtra("BOOK_ID", -1);
        bookService = RetrofitClient.getClient(this).create(BookService.class);
        commentsAdapter = new CommentsAdapter(this, commentList);
        commentRecyclerView.setAdapter(commentsAdapter);

        if (bookId != -1) {
            fetchBookDetails(bookId);
        }

        //Star event read book
        btnStarReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchFileBook();
            }
        });

        btnAddToShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addBookShelf(MainActivity.userId,bookId);// Replace with actual
            }
        });
    }


    private void addBookShelf(Long userId, Long bookId) {
        if (book != null && !book.getLink().isEmpty()) {
            Call<BookResponse> call = bookService.addBookShelf(userId, bookId);
            call.enqueue(new Callback<BookResponse>() {
                @Override
                public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookDetailActivity.this, "Add successfully!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle unsuccessful response (e.g., HTTP error codes)
                        Toast.makeText(BookDetailActivity.this, "This Book already added!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BookResponse> call, Throwable t) {
                    // Handle failure, typically due to network issues
                    Toast.makeText(BookDetailActivity.this, "Failed to add book: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(BookDetailActivity.this, "Book details are not available or invalid.", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchFileBook() {
        if(book != null && !book.getLink().isEmpty()){
            Call<UrlResponse> call = bookService.getFileBookToAws(book.getLink());
            call.enqueue(new Callback<UrlResponse>() {
                @Override
                public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                    if(response.isSuccessful() && response.body() != null){
                        String url = response.body().getData().getUrl();
                        Intent intent = new Intent(BookDetailActivity.this, ReadFileBookActivity.class);
                        intent.putExtra("BOOK_URL", url);
                        intent.putExtra("BOOK_ID", book.getId());
                        intent.putExtra("BOOK_NAME", book.getName());
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<UrlResponse> call, Throwable t) {
                    Log.e("API Error", "Failure: " + t.getMessage());
                }
            });
        }
    }

    private void fetchBookDetails(Long id) {
        // Make an API call to fetch new books and update the adapter
        Call<BookResponse> call = bookService.getBookById(id);
        call.enqueue(new Callback<BookResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    book = response.body().getData().getBook();
                    Log.i("API Response", book.toString());

                    Glide.with(BookDetailActivity.this).load(book.getThumbnail()).into(bookCover);
                    Picasso.get().load(book.getThumbnail()).into(bookCoverBlur, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            // Apply blur effect once the image is loaded
                            bookCoverBlur.post(() -> {
                                Blurry.with(BookDetailActivity.this)
                                        .radius(10)
                                        .sampling(2)
                                        .color(Color.argb(66, 255, 255, 0))
                                        .async()
                                        .capture(bookCoverBlur)
                                        .into(bookCoverBlur);
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });

                    bookTitle.setText(book.getName());
                    bookAuthor.setText(book.getAuthor().getName());

                    if (book.getFeedbacks() != null) {
                        commentList.clear();
                        int maxComments = Math.min(book.getFeedbacks().size(), 4);
                        for (int i = 0; i < maxComments; i++) {
                            commentList.add(book.getFeedbacks().get(i));
                        }
                        commentsAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("API Error", "Feedback list is null");
                    }

                    String fullText = book.getIntroduce();
                    if (fullText != null) {
                        if (fullText.length() > MAX_LENGTH) {
                            bookDescription.setText(fullText.substring(0, MAX_LENGTH) + "...");
                        } else {
                            bookDescription.setText(fullText);
                        }

                        bookDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isExpanded) {
                                    if (fullText.length() > MAX_LENGTH) {
                                        bookDescription.setText(fullText.substring(0, MAX_LENGTH) + "...");
                                    } else {
                                        bookDescription.setText(fullText);
                                    }
                                    bookDescription.setMaxLines(fullText.length());
                                    bookDescription.setEllipsize(TextUtils.TruncateAt.END);
                                } else {
                                    bookDescription.setText(fullText);
                                    bookDescription.setMaxLines(fullText.length());
                                    bookDescription.setEllipsize(null);
                                }
                                isExpanded = !isExpanded;
                            }
                        });
                    } else {
                        Log.e("API Error", "Book introduction is null");
                    }
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
}
