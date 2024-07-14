package com.example.frontend.adapters.menubookmark;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontend.R;
import com.example.frontend.activities.MenuBookMarkActivity;
import com.example.frontend.activities.ReadFileBookActivity;
import com.example.frontend.models.BookMark;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.services.BookMarkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkAdapter extends ArrayAdapter<BookMark> {
    private Context context;
    private List<BookMark> bookMarks;

    private Long bookId;
    private String bookUrl, bookName;

    public BookmarkAdapter(@NonNull Context context, @NonNull List<BookMark> bookMarks, Long bookId, String bookUrl, String bookName) {
        super(context, 0, bookMarks);
        this.context = context;
        this.bookMarks = bookMarks;
        this.bookId = bookId;
        this.bookUrl = bookUrl;
        this.bookName = bookName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false);
        }
        BookMark bookMark = bookMarks.get(position);
        TextView tvNameBookMark = convertView.findViewById(R.id.tvNameBookmark);
        Button btnService = convertView.findViewById(R.id.btnService);
        tvNameBookMark.setText(bookMark.getName());
        tvNameBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadFileBookActivity.class);
                intent.putExtra("BOOK_URL", bookUrl);
                intent.putExtra("BOOK_ID", bookId);
                intent.putExtra("BOOK_NAME", bookName);
                intent.putExtra("CURRENT_PAGE", bookMark.getPageNumber());
                context.startActivity(intent);
            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, bookMark.getId());
            }
        });
        return convertView;
    }

    private void showPopupMenu(View view, Long bookmarkId) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.service_bookmark, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.itemDelete) {
                    BookMarkService bookMarkService = RetrofitClient.getClient(context).create(BookMarkService.class);
                    Call<DataResponse> call = bookMarkService.deleteById(bookmarkId);
                    call.enqueue(new Callback<DataResponse>() {
                        @Override
                        public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                            if (response.isSuccessful()) {
                                bookMarks.removeIf(bookMark -> bookMark.getId().equals(bookmarkId));
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponse> call, Throwable t) {
                            Log.e("API Error", "Failure: " + t.getMessage());
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }
}