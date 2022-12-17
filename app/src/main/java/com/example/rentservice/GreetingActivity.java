package com.example.rentservice;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.util.*;
import com.example.rentservice.databinding.ActivityGreetingBinding;

import java.util.Objects;

public class GreetingActivity extends AppCompatActivity {
    ActivityGreetingBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        b = ActivityGreetingBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        UserData usrd = new SBHelper(this).getUserData();
        if (usrd.getUser().getId()==0) {
            b.logoMain.animate().alpha(1).setDuration(1000).setStartDelay(1250);
            b.linkReg.animate().alpha(1).setDuration(1000).setStartDelay(1250);
            b.loginMain.animate().alpha(1).setDuration(1000).setStartDelay(1250);

            b.linkReg.setOnClickListener(v -> {
                Intent i = new Intent(GreetingActivity.this, AuthActivity.class);
                i.putExtra("fragment", 1);
                startActivity(i);
                finish();
            });
            b.loginMain.setOnClickListener(v -> {
                startActivity(new Intent(GreetingActivity.this, AuthActivity.class));
                finish();
            });
        } else {
            b.logoMain.animate().alpha(1).setDuration(1000).setStartDelay(1250).withEndAction(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", usrd.getUser().getUsername());
                intent.putExtra("userid", usrd.getUser().getId());
                intent.putExtra("token", usrd.getToken());
                startActivity(intent);
                finish();
            });

        }
    }
}