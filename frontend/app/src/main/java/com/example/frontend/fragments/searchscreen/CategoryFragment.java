package com.example.frontend.fragments.searchscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontend.R;

public class CategoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_search_screen, container, false);
        return view;
    }

    public void searchCategory(String query) {
        Toast.makeText(getContext(), "Searching books by author: " + query, Toast.LENGTH_SHORT).show();
    }
}
