package com.example.frontend.fragments.notificationscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.BookDetailActivity;
import com.example.frontend.adapters.notificationscreen.NotificationAdapter;
import com.example.frontend.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationItemListener {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference("Notification")
                .orderByChild("timestamp")  // Assuming "timestamp" is the field you're sorting by
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notificationList.clear();
                        List<Notification> tempList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            tempList.add(notification);
                        }
                        for (int i = tempList.size() - 1; i >= 0; i--) {
                            notificationList.add(tempList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("API Error", "Failure: " + error.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        Notification clickedNotification = notificationList.get(position);
        Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
        intent.putExtra("BOOK_ID", clickedNotification.getBookId());
        startActivity(intent);
    }
}
