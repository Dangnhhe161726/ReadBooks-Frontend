package com.example.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.models.Book;
import com.example.frontend.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.BookViewHolder> {
    private List<Book> bookList;
    private Context context;


    public BookshelfAdapter(Context context,List<Book> bookList) {
        this.context = context;

        this.bookList = bookList;
    }
    @NonNull
    @Override
    public BookshelfAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookshelfAdapter.BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        Picasso.get().load(book.getThumbnail()).into(holder.ivBookCover);
        holder.tvBookTitle.setText(book.getName());
        holder.tvBookAuthor.setText(book.getAuthor().getName());
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
    }
}
