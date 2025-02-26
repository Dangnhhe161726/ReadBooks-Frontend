package com.example.frontend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.models.FeedBack;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.responses.UserResponse;
import com.example.frontend.services.UserService;
import de.hdodenhof.circleimageview.CircleImageView;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.ChronoUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackDetailActivity extends AppCompatActivity {
    private Button btnExit;
    private CircleImageView cImgUserFeedbackDetail;
    private TextView tvUserNameDetail, tvCreateTimeFeedBackDetail, tvContentFeedbackDetail, tvNumberLikeFeedBackDetail, tvNumberCommentFeedbackDetail;
    private ImageButton imgBtnNumberLikeFeedBackDetail;
    private RecyclerView recyclerViewFeedbackDetail;
    private String keyFeedback;
    private FeedBack feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);
        keyFeedback = getIntent().getStringExtra("KEY_FEEDBACK");
        feedBack = (FeedBack) getIntent().getSerializableExtra("FEEDBACK");
        if (keyFeedback == null && feedBack == null) {
            finish();
            return;
        }
        initView();
        updateUI(feedBack);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void updateUI(FeedBack feedback) {
        fetchUser(feedback.getUserId());
        String createTime = feedback.getCreateTime();
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
                tvCreateTimeFeedBackDetail.setText(timeDifference);
            }
        } catch (DateTimeParseException e) {
            tvCreateTimeFeedBackDetail.setText("Invalid date");
        }
        tvContentFeedbackDetail.setText(feedback.getContent());
        tvNumberLikeFeedBackDetail.setText(feedback.getNumberLike() + "");
        tvNumberCommentFeedbackDetail.setText(feedback.getNumberComment() + "");
    }

    private void fetchUser(Long userId) {
        UserService userService = RetrofitClient.getClient(this).create(UserService.class);
        Call<DataResponse> call = userService.getById(userId);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body().getData().getUser();
                    Glide.with(FeedbackDetailActivity.this)
                            .load(user.getAvatar())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_error)
                            .into(cImgUserFeedbackDetail);
                    tvUserNameDetail.setText(user.getFullName() == null ? "Unknown User" : user.getFullName());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable throwable) {
                cImgUserFeedbackDetail.setImageResource(R.drawable.img_error);
                tvUserNameDetail.setText("Unknown User");
            }
        });
    }

    private void initView() {
        btnExit = findViewById(R.id.btnExit);
        cImgUserFeedbackDetail = findViewById(R.id.cImgUserFeedbackDetail);
        tvUserNameDetail = findViewById(R.id.tvUserNameDetail);
        tvCreateTimeFeedBackDetail = findViewById(R.id.tvCreateTimeFeedBackDetail);
        tvContentFeedbackDetail = findViewById(R.id.tvContentFeedbackDetail);
        tvNumberLikeFeedBackDetail = findViewById(R.id.tvNumberLikeFeedBackDetail);
        tvNumberCommentFeedbackDetail = findViewById(R.id.tvNumberCommentFeedbackDetail);
        imgBtnNumberLikeFeedBackDetail = findViewById(R.id.imgBtnNumberLikeFeedBackDetail);
    }
}
