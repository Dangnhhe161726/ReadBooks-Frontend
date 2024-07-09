package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ReadBookActivity extends AppCompatActivity {

    private PDFView pdfView;
    private Button btnStopReading;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_book);

        pdfView = findViewById(R.id.pdfView);
        btnStopReading = findViewById(R.id.btnStopReading);

        // Nhận đường dẫn file từ intent
        filePath = getIntent().getStringExtra("filePath");

        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                pdfView.fromFile(file)
                        .spacing(10) // khoảng cách giữa các trang (dp)
                        .load();
            } else {
                Toast.makeText(this, "Không thể tải PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Đường dẫn file PDF không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        btnStopReading.setOnClickListener(v -> {
            stopReading();
        });
    }

    private void stopReading() {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                Toast.makeText(this, "Book file deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete book file", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(ReadBookActivity.this, BookDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
