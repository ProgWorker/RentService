package com.example.rentservice;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

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
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
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
            b.logoMain.animate().alpha(1).setDuration(250).setStartDelay(250).withEndAction(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", usrd.getUser().getUsername());
                intent.putExtra("userid", usrd.getUser().getId());
                intent.putExtra("token", usrd.getToken());
                startActivity(intent);
                finish();
            });

        }
    }
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(GreetingActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GreetingActivity.this, new String[] { permission }, requestCode);
        }
        /*else {
            //Toast.makeText(GreetingActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }*/
    }
}