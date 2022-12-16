package com.example.rentservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.navigation.NavController;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentservice.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    ActivityMainBinding binding;
    Intent data;
    ImageView avatar;
    int currId = R.id.nav_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        data = getIntent();
        navigationView = binding.bNav;
        navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnItemSelectedListener(v -> {
            int id = navigationView.getSelectedItemId();
            switch (v.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(
                                    R.id.nav_host_fragment_content_drawer, new HomeFragment())
                            .commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(
                                    R.id.nav_host_fragment_content_drawer, new ProfileFragment())
                            .commit();
                    break;
            }
            return true;
        });
        /*findViewById(R.id.exit).setOnClickListener(v -> {
            System.exit(0);
        });*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setSelectedItemId(currId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}