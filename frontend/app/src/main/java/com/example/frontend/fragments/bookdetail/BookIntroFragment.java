package com.example.frontend.fragments.bookdetail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontend.R;

public class BookIntroFragment extends Fragment {

    private TextView tvIntroduce;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_intro, container, false);
        tvIntroduce = view.findViewById(R.id.tvIntroduce);

        // Load and display book introduction here if needed
        // Example: tvIntroduce.setText(book.getIntroduce());

        return view;
    }
    public void setIntroduceText(String introduce) {
        if (tvIntroduce != null) {
            tvIntroduce.setText(introduce);
        }
    }
}
