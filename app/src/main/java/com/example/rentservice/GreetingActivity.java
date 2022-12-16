package com.example.rentservice;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.example.rentservice.databinding.ActivityGreetingBinding;

public class GreetingActivity extends AppCompatActivity {
    ActivityGreetingBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        b = ActivityGreetingBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        getSupportActionBar().hide();
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
    }
}