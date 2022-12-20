package com.example.rentservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Objects.requireNonNull(getSupportActionBar()).hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(getIntent().getIntExtra("fragment", 0)==1)
            transaction.replace(R.id.fragment_container_view, new RegistrationFragment());
        else
            transaction.replace(R.id.fragment_container_view, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}