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
import com.example.frontend.activities.BookDetailActivity;
import com.example.frontend.models.FeedBack;



import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<FeedBack> commentList;
    private Context context;

    public CommentsAdapter(Context context, List<FeedBack> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        FeedBack comment = commentList.get(position);
        holder.userName.setText(comment.getUserEntity().getFullName());
        holder.text.setText(comment.getContent());
        Glide.with(context).load(comment.getUserEntity().getAvatar()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userName, text;
        ImageView userImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.comment_user_name);
            text = itemView.findViewById(R.id.comment_text);
            userImage = itemView.findViewById(R.id.comment_user_image);
        }
    }
}
