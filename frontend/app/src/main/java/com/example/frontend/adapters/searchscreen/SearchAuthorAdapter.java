package com.example.frontend.adapters.searchscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.event.OnAuthorClickListener;
import com.example.frontend.models.Author;

import java.util.ArrayList;
import java.util.List;

public class SearchAuthorAdapter extends RecyclerView.Adapter<SearchAuthorAdapter.ViewHolder> {
    private List<Author> authors = new ArrayList<>();
    private OnAuthorClickListener onAuthorClickListener;

    public SearchAuthorAdapter(OnAuthorClickListener onAuthorClickListener) {
        this.onAuthorClickListener = onAuthorClickListener;
    }

    public void addAuthors(List<Author> authors) {
        int previousSize = this.authors.size();
        this.authors.addAll(authors);
        notifyItemRangeInserted(previousSize, authors.size());
    }

    public void clearAuthors() {
        authors.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Author author = authors.get(position);
        holder.textViewAuthorName.setText(author.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onAuthorClickListener != null) {
                onAuthorClickListener.onAuthorClick(author);
            }
        });
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthorName = itemView.findViewById(R.id.tvAuthorName);
        }
    }
}
