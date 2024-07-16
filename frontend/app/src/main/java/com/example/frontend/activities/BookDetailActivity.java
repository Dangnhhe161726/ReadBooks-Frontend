package com.example.frontend.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapters.menufeedback.FeedbackAdapter;
import com.example.frontend.models.Book;
import com.example.frontend.models.FeedBack;
import com.example.frontend.models.UserByToken;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.BookResponse;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.responses.UrlResponse;
import com.example.frontend.services.BookService;
import com.example.frontend.services.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;

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
    private TextView bookTitle, bookAuthor, bookDescription;
    private RecyclerView commentRecyclerView;
    private Button btnStarReading;
    private Book book;
    //feature Feedback
    private FeedbackAdapter adapter;
    private List<FeedBack> feedBacks;
    private ImageButton imgBtnNewComment;
    private Long bookId;
    private UserByToken user;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookdetail_layout);
        // Retrieve the book ID from the intent
        bookId = getIntent().getLongExtra("BOOK_ID", -1);
        bookService = RetrofitClient.getClient(this).create(BookService.class);

        if (bookId != -1) {
            fetchBookDetails(bookId);
        } else {
            Toast.makeText(this, "Invalid book information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book = new Book();
        bookCoverBlur = findViewById(R.id.blurred_background);
        bookTitle = findViewById(R.id.book_title);
        bookCover = findViewById(R.id.book_cover);
        bookAuthor = findViewById(R.id.book_author);


        bookDescription = findViewById(R.id.book_description);
        btnStarReading = findViewById(R.id.btn_start_reading);
        Button btnAddToShelf = findViewById(R.id.btn_add_to_shelf);
        //feature feedback
        commentRecyclerView = findViewById(R.id.comments_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserService userService = RetrofitClient.getClient(this).create(UserService.class);
        Call<DataResponse> call = userService.getUserInfor();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body().getData() != null) {
                    user = response.body().getData().getUserByToken();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("Api failed", "Request Failed: " + t.getMessage());
            }
        });
        feedBacks = new ArrayList<>();
        adapter = new FeedbackAdapter(feedBacks, this);
        commentRecyclerView.setAdapter(adapter);
        fetchFeedbacksByBookId(bookId);
        imgBtnNewComment = findViewById(R.id.imgBtnNewComment);
        imgBtnNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Feedback");
                showAddFeedbackDialog();
            }
        });
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
                addBookShelf(MainActivity.userId, bookId);// Replace with actual
            }
        });
    }

    private void showAddFeedbackDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_feedback);
        EditText edtFeedbackContent = dialog.findViewById(R.id.edtFeedbackContent);
        Button btnSubmitFeedback = dialog.findViewById(R.id.btnSubmitFeedback);
        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtFeedbackContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(BookDetailActivity.this, "Please enter feedback content", Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalDateTime currentDateTime = LocalDateTime.now();

                FeedBack feedBack = new FeedBack();
                feedBack.setContent(content);
                feedBack.setBookId(bookId);
                feedBack.setNumberComment(0);
                feedBack.setNumberLike(0);
                feedBack.setUserId(user.getId());
                feedBack.setCreateTime(currentDateTime.toString());
                databaseReference.push().setValue(feedBack);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void fetchFeedbacksByBookId(Long bookId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback");
        databaseReference.orderByChild("bookId").equalTo(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedBacks.clear();
                List<FeedBack> tempList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FeedBack feedback = dataSnapshot.getValue(FeedBack.class);
                    if(feedback != null){
                        feedback.setKey(dataSnapshot.getKey());
                        tempList.add(feedback);
                    }
                }
                for (int i = tempList.size() - 1; i >= 0; i--) {
                    feedBacks.add(tempList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Request Failed: " + error.getMessage());
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
        if (book != null && !book.getLink().isEmpty()) {
            Call<UrlResponse> call = bookService.getFileBookToAws(book.getLink());
            call.enqueue(new Callback<UrlResponse>() {
                @Override
                public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
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
