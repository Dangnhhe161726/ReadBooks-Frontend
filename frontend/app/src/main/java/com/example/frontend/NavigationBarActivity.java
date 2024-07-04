package com.example.frontend;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.frontend.R;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.fragments.MyBookFragment;
import com.example.frontend.fragments.MyProfileFragment;
import com.example.frontend.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationBarActivity extends AppCompatActivity {
    private LinearLayout categoryContainer;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_custom);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frame_layout);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navSearch) {
                    loadFragment(new SearchFragment(), false);
                } else if (itemId == R.id.navMyBook) {
                    loadFragment(new MyBookFragment(), false);
                } else {
                    loadFragment(new MyProfileFragment(), false);
                }
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment, boolean isInit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isInit) {
            fragmentTransaction.add(R.id.frame_layout, fragment);

        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}