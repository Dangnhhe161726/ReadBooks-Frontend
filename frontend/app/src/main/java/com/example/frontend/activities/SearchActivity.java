package com.example.frontend.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.viewpager2.widget.ViewPager2;

import com.example.frontend.R;
import com.example.frontend.adapters.searchscreen.FragmentAdapter;
import com.example.frontend.fragments.searchscreen.AuthorFragment;
import com.example.frontend.fragments.searchscreen.BookFragment;
import com.example.frontend.fragments.searchscreen.CategoryFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class SearchActivity extends AppCompatActivity {
    private SearchView searchViewSearch;
    private TabLayout tabLayoutSearch;
    private ViewPager2 viewPager2Search;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        initView();
        setupSearchView();
    }

    private void setupSearchView() {
        searchViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void performSearch(String query) {
        int currentItem = viewPager2Search.getCurrentItem();
        switch (currentItem) {
            case 0:
                AuthorFragment authorFragment = (AuthorFragment) adapter.getFragment(currentItem);
                authorFragment.searchAuthors(query);
                break;
            case 1:
                BookFragment bookFragment = (BookFragment) adapter.getFragment(currentItem);
                bookFragment.searchBooks(query);
                bookFragment.setQuery(query);
                break;
            case 2:
                CategoryFragment categoryFragment = (CategoryFragment) adapter.getFragment(currentItem);
                categoryFragment.searchCategory(query);
                break;
        }
    }

    private void initView() {
        searchViewSearch = findViewById(R.id.searchViewSearch);
        tabLayoutSearch = findViewById(R.id.tabLayoutSearch);
        viewPager2Search = findViewById(R.id.viewPager2Search);
        adapter = new FragmentAdapter(this);
        viewPager2Search.setAdapter(adapter);
        //Nối tablayout với fragmentAdapter
        new TabLayoutMediator(tabLayoutSearch, viewPager2Search,
                (tab, position) -> tab.setText(adapter.getTabTitle(position))
        ).attach();
    }
}
