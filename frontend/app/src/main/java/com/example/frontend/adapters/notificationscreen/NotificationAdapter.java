package com.example.frontend.adapters.notificationscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.models.Notification;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private NotificationItemListener notificationItemListener;
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public NotificationItemListener getNotificationItemListener() {
        return notificationItemListener;
    }

    public void setNotificationItemListener(NotificationItemListener notificationItemListener) {
        this.notificationItemListener = notificationItemListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        String url = notification.getLinkBook();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(holder.cimgNotification);

        String title = notification.getTitle();
        String introduce = notification.getDescription();

        if (!title.isEmpty()) {
            title = title.length() > 31 ? title.substring(0, 31) + "..." : title;
        }

        if (!introduce.isEmpty()) {
            introduce = introduce.length() > 195 ? introduce.substring(0, 195) + "..." : introduce;
        }

        holder.tvTitle.setText(title);
        holder.tvIntroduce.setText(introduce);

        String createTime = notification.getCreateTime();
        try {
            if (createTime != null && !createTime.isEmpty()) {
                LocalDateTime parsedDateTime = LocalDateTime.parse(createTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime currentDateTime = LocalDateTime.now();

                long daysBetween = ChronoUnit.DAYS.between(parsedDateTime, currentDateTime);
                long monthsBetween = ChronoUnit.MONTHS.between(parsedDateTime, currentDateTime);
                long yearsBetween = ChronoUnit.YEARS.between(parsedDateTime, currentDateTime);

                String timeDifference;
                if (yearsBetween > 0) {
                    timeDifference = yearsBetween + " năm trước";
                } else if (monthsBetween > 0) {
                    timeDifference = monthsBetween + " tháng trước";
                } else {
                    timeDifference = daysBetween + " ngày trước";
                }

                holder.tvCreateTime.setText(timeDifference);
            }
        } catch (DateTimeParseException e) {
            holder.tvCreateTime.setText("Invalid date");
        }
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    public interface NotificationItemListener {
        void onItemClick(View view, int position);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView cimgNotification;
        TextView tvTitle, tvIntroduce, tvCreateTime;

        public NotificationViewHolder(@NonNull View view) {
            super(view);
            cimgNotification = view.findViewById(R.id.cImgNotification);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvIntroduce = view.findViewById(R.id.tvIntroduce);
            tvCreateTime = view.findViewById(R.id.tvCreateTime);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (notificationItemListener != null) {
                notificationItemListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
