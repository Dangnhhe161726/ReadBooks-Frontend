package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.frontend.fragments.HomePageFragment;
import com.example.frontend.fragments.MyBookFragment;
import com.example.frontend.fragments.MyProfileFragment;
import com.example.frontend.fragments.searchscreen.SearchFragment;
import com.example.frontend.models.UserByToken;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.DataResponse;
import com.example.frontend.services.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private UserService userService;

    public static Long userId;
    private UserByToken user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.fragmentContainer);
        userService = RetrofitClient.getClient(this).create(UserService.class);
        getUser();
        if (savedInstanceState == null) {
            loadFragment(new HomePageFragment(), true);
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.navHome) {
                    loadFragment(new HomePageFragment(), false);
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

    private void getUser() {
        Call<DataResponse> call = userService.getUserInfor();

        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body().getData().getUserByToken();
                    if (user != null) {
                        userId = user.getId();
                    } else {
                        Log.e("API Error", "User is null");
                    }
                } else {
                    Log.e("API Error", "Failed to get user info: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isInit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isInit) {
            fragmentTransaction.add(R.id.fragmentContainer, fragment);

        } else {
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        }
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}