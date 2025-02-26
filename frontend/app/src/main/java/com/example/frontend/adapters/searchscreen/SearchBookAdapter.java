package com.example.frontend.adapters.searchscreen;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.CategoryDetailActivity;
import com.example.frontend.models.Book;
import com.example.frontend.models.Category;

import java.util.ArrayList;
import java.util.List;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.SearchBookViewHolder> {

    private List<Book> books = new ArrayList<>();
    private SearchBookItemListener searchBookItemListener;

    public SearchBookAdapter() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public SearchBookItemListener getSearchBookItemListener() {
        return searchBookItemListener;
    }

    public void setSearchBookItemListener(SearchBookItemListener searchBookItemListener) {
        this.searchBookItemListener = searchBookItemListener;
    }

    @NonNull
    @Override
    public SearchBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_book_screen, parent, false);
        return new SearchBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookViewHolder holder, int position) {
        Book book = books.get(position);
        String url = book.getThumbnail();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(holder.imgISearch);
        holder.tvNameISearch.setText(book.getName());
        holder.tvAuthorISearch.setText(book.getAuthor().getName());
        holder.tvFavoritesISearch.setText(String.format("%d", book.getFavorites()));
        if (book.getCategories() == null || book.getCategories().isEmpty()) {
            holder.btnCategoryISearch.setText("Uncategorized yet");
        } else {
            holder.btnCategoryISearch.setText(book.getCategories().get(0).getName());
        }
    }

    public void addBooks(List<Book> bookList) {
        if (bookList != null) {
            books.addAll(bookList);
            notifyDataSetChanged();
        }
    }

    public void clearBooks() {
        books.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface SearchBookItemListener {
        void onItemClick(View view, int position);
    }

    public class SearchBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgISearch;
        TextView tvNameISearch, tvAuthorISearch, tvFavoritesISearch;
        Button btnCategoryISearch;

        public SearchBookViewHolder(@NonNull View view) {
            super(view);
            imgISearch = view.findViewById(R.id.imgISearch);
            tvNameISearch = view.findViewById(R.id.tvNameISearch);
            tvAuthorISearch = view.findViewById(R.id.tvAuthorISearch);
            tvFavoritesISearch = view.findViewById(R.id.tvFavoritesISearch);
            btnCategoryISearch = view.findViewById(R.id.btnCategoryISearch);
            view.setOnClickListener(this);
            btnCategoryISearch.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Book book = books.get(position);
                    if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                        Category category = book.getCategories().get(0);
                        Intent intent = new Intent(v.getContext(), CategoryDetailActivity.class);
                        intent.putExtra("category_id", category.getId());
                        intent.putExtra("category_name", category.getName());
                        v.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), "No category assigned to this book", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (searchBookItemListener != null) {
                searchBookItemListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
