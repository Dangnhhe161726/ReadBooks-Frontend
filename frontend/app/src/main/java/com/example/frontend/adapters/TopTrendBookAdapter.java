package com.example.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.event.OnBookClickListener;
import com.example.frontend.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopTrendBookAdapter extends RecyclerView.Adapter<TopTrendBookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;
    private OnBookClickListener listener;


    public TopTrendBookAdapter(Context context, List<Book> books, OnBookClickListener listener) {
        this.context = context;
        this.bookList = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBookCover;
        TextView tvBookTitle;
        TextView tvBookAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
        }

        public void bind(final Book book, final OnBookClickListener listener) {
            // Assuming your Book class has getCoverUrl(), getTitle(), and getAuthor() methods
            Glide.with(itemView.getContext()).load(book.getThumbnail()).into(ivBookCover);
            tvBookTitle.setText(book.getName());
            tvBookAuthor.setText(book.getAuthor().getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}
