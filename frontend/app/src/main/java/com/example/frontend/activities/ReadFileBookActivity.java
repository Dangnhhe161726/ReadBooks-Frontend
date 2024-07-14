package com.example.frontend.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frontend.R;
import com.example.frontend.models.BookMark;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.requests.BookMarkRequest;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.services.BookMarkService;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadFileBookActivity extends AppCompatActivity {

    private PDFView pdfView;
    private Button btnExit, btnBookMark, btnMenuBookMark;
    private File downloadedFile;
    private String bookUrl;
    private Long bookId;
    private String bookName;
    private boolean isRelativeLayoutVisible = false;
    private RelativeLayout relativeLayout;
    private BookMarkService bookMarkService;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.read_file_book);
        bookUrl = getIntent().getStringExtra("BOOK_URL");
        bookId = getIntent().getLongExtra("BOOK_ID", -1);
        bookName = getIntent().getStringExtra("BOOK_NAME");
        int bookmarkedPage = getIntent().getIntExtra("CURRENT_PAGE", -1);
        if (bookmarkedPage != -1) {
            currentPage = bookmarkedPage;
        }
        initView();
        setupPdfViewClickListener();
        exitScreen();
        bookmark();
        viewBookMark();
        if (bookUrl != null && bookId != -1) {
            new DownloadFileTask().execute(bookUrl);
        } else {
            Log.e("ReadFileBookActivity", "Failed to get book data from intent");
        }
    }

    private void initView() {
        pdfView = findViewById(R.id.pdfView);
        btnExit = findViewById(R.id.btnExit);
        btnBookMark = findViewById(R.id.btnBookMark);
        btnMenuBookMark = findViewById(R.id.btnMenuBookMark);
        relativeLayout = findViewById(R.id.relativeLayout);
        bookMarkService = RetrofitClient.getClient(this).create(BookMarkService.class);
    }

    private void viewBookMark() {
        btnMenuBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadFileBookActivity.this, MenuBookMarkActivity.class);
                intent.putExtra("BOOK_URL", bookUrl);
                intent.putExtra("BOOK_ID", bookId);
                intent.putExtra("BOOK_NAME", bookName);
                startActivity(intent);
            }
        });
    }

    private void bookmark() {
        btnBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookMarkRequest bookMark = new BookMarkRequest();
                bookMark.setBookId(bookId);
                if (bookName != null) {
                    bookMark.setName(bookName + "_" + currentPage);
                } else {
                    bookMark.setName("_" + currentPage);
                }
                bookMark.setPageNumber(currentPage - 1);
                if (bookMark != null) {
                    saveBookMark(bookMark);
                }
            }
        });
    }

    private void saveBookMark(BookMarkRequest bookMark) {
        Call<DataResponse> call = bookMarkService.create(bookMark);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookMark newBookMark = response.body().getData().getBookMark();
                    Toast.makeText(ReadFileBookActivity.this, "Đã thêm dấu trang " + newBookMark.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void exitScreen() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDownloadedFile();
            }
        });
    }

    private void deleteDownloadedFile() {
        if (downloadedFile != null && downloadedFile.exists()) {
            boolean deleted = downloadedFile.delete();
            if (deleted) {
                pdfView.recycle();
                pdfView.setVisibility(View.GONE);
                downloadedFile = null;
                Log.d("File Deletion", "File deleted successfully.");
                Intent intent = new Intent(ReadFileBookActivity.this, BookDetailActivity.class);
                intent.putExtra("BOOK_ID", bookId);
                startActivity(intent);
            } else {
                Log.e("File Deletion", "Failed to delete the file.");
            }
        } else {
            Log.e("File Deletion", "File not found or does not exist.");
        }
    }

    private void setupPdfViewClickListener() {
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRelativeLayout();
            }
        });
    }

    private void toggleRelativeLayout() {
        if (isRelativeLayoutVisible) {
            relativeLayout.setVisibility(View.GONE);
            isRelativeLayoutVisible = false;
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            isRelativeLayoutVisible = true;
        }
    }

    private class DownloadFileTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            String fileUrl = strings[0];
            File file = new File(getFilesDir(), "downloaded.pdf");

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.close();
                inputStream.close();

                return file;

            } catch (IOException e) {
                Log.e("Download Error", "Error downloading file: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                downloadedFile = file;
                displayPdf(file);
            } else {
                Log.e("Download Error", "Failed to download file.");
            }
        }
    }

    private void displayPdf(File file) {
        pdfView.fromFile(file)
                .defaultPage(currentPage)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageChange((page, pageCount) -> currentPage = page)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .fitEachPage(true)
                .load();
        pdfView.setVisibility(View.VISIBLE);
    }
}