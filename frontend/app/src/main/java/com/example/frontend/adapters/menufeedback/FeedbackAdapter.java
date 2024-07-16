package com.example.frontend.adapters.menufeedback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.FeedbackDetailActivity;
import com.example.frontend.models.FeedBack;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.responses.UserResponse;
import com.example.frontend.services.AuthorService;
import com.example.frontend.services.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeebackViewHolder> {
    private List<FeedBack> feedBacks;
    private Context context;

    public FeedbackAdapter(List<FeedBack> feedBacks, Context context) {
        this.feedBacks = feedBacks;
        this.context = context;
    }

    public List<FeedBack> getFeedBacks() {
        return feedBacks;
    }

    public void setFeedBacks(List<FeedBack> feedBacks) {
        this.feedBacks = feedBacks;
    }


    @NonNull
    @Override
    public FeebackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new FeebackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeebackViewHolder holder, int position) {
        FeedBack feedBack = feedBacks.get(position);
        AuthorService authorService = RetrofitClient.getClient(context).create(AuthorService.class);
        Call<DataResponse> call = authorService.getProfile();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body().getData().getUser();
                    Glide.with(holder.itemView.getContext())
                            .load(userResponse.getAvatar())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_error)
                            .into(holder.cImgUser);
                    holder.tvNameUser.setText(
                            userResponse.getFullName() != null ? userResponse.getFullName() : "Unknown User");
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("API Error", "Request Failed: " + t.getMessage());
                holder.tvNameUser.setText("Unknown User");
                holder.cImgUser.setImageResource(R.drawable.img_error);
            }
        });
        holder.tvContent.setText(feedBack.getContent());
        holder.numberLike.setText(feedBack.getNumberLike() + "");
        holder.numberComment.setText(feedBack.getNumberComment() + "");
        String createTime = feedBack.getCreateTime();
        try {
            if (createTime != null && !createTime.isEmpty()) {
                LocalDateTime parsedDateTime = LocalDateTime.parse(createTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime currentDateTime = LocalDateTime.now();

                long minutesBetween = ChronoUnit.MINUTES.between(parsedDateTime, currentDateTime);
                long hoursBetween = ChronoUnit.HOURS.between(parsedDateTime, currentDateTime);
                long daysBetween = ChronoUnit.DAYS.between(parsedDateTime, currentDateTime);
                long monthsBetween = ChronoUnit.MONTHS.between(parsedDateTime, currentDateTime);
                long yearsBetween = ChronoUnit.YEARS.between(parsedDateTime, currentDateTime);

                String timeDifference;
                if (yearsBetween > 0) {
                    timeDifference = yearsBetween + " năm trước";
                } else if (monthsBetween > 0) {
                    timeDifference = monthsBetween + " tháng trước";
                } else if (daysBetween > 0) {
                    timeDifference = daysBetween + " ngày trước";
                } else if (hoursBetween > 0) {
                    timeDifference = hoursBetween + " tiếng trước";
                } else {
                    timeDifference = minutesBetween + " phút trước";
                }
                holder.tvCreateTimeUser.setText(timeDifference);
            }
        } catch (DateTimeParseException e) {
            holder.tvCreateTimeUser.setText("Invalid date");
        }
        holder.imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("Feedback").child(feedBack.getKey());
                feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FeedBack updatedFeedback = snapshot.getValue(FeedBack.class);
                        if (updatedFeedback != null) {
                            int currentLikes = updatedFeedback.getNumberLike();
                            updatedFeedback.setNumberLike(currentLikes + 1);
                            feedbackRef.setValue(updatedFeedback);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase Error", "Failed to update like: " + error.getMessage());
                    }
                });
            }
        });
        holder.imgBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), FeedbackDetailActivity.class);
                intent.putExtra("KEY_FEEDBACK", feedBack.getKey());
                intent.putExtra("FEEDBACK", feedBack);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedBacks != null ? feedBacks.size() : 0;
    }

    public class FeebackViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cImgUser;
        TextView tvNameUser, tvCreateTimeUser, tvContent, numberLike, numberComment;
        ImageButton imgBtnComment, imgBtnLike;

        public FeebackViewHolder(@NonNull View view) {
            super(view);
            cImgUser = view.findViewById(R.id.cImgUser);
            tvNameUser = view.findViewById(R.id.tvNameUser);
            tvCreateTimeUser = view.findViewById(R.id.tvCreateTimeUser);
            tvContent = view.findViewById(R.id.tvContent);
            numberLike = view.findViewById(R.id.numberLike);
            numberComment = view.findViewById(R.id.numberComment);
            imgBtnComment = view.findViewById(R.id.imgBtnComment);
            imgBtnLike = view.findViewById(R.id.imgBtnLike);
        }
    }

}
