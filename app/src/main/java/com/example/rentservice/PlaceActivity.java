package com.example.rentservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.rentservice.databinding.ActivityPlaceBinding;

public class PlaceActivity extends AppCompatActivity {
    ActivityPlaceBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPlaceBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.recDetail.setOnClickListener(view -> {
            LayoutInflater m = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = m.inflate(R.layout.comments, null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(v, width, height, focusable);
            v.findViewById(R.id.button).setOnClickListener(vi -> {
                Toast.makeText(getApplicationContext(),"comment sended", Toast.LENGTH_SHORT).show();
            });
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }
}