package com.example.frontend.adapters.searchscreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.fragments.searchscreen.AuthorFragment;
import com.example.frontend.fragments.searchscreen.BookFragment;
import com.example.frontend.fragments.searchscreen.CategoryFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{
                new AuthorFragment(),
                new BookFragment(),
                new CategoryFragment()
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    public String getTabTitle(int position) {
        switch (position) {
            case 0: return "Author";
            case 1: return "Book";
            case 2: return "Category";
            // Add more titles here
            default: return null;
        }
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }
}
