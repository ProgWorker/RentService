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

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.CPlace;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.databinding.ActivityPlaceBinding;
import com.example.rentservice.util.callbacks.RetrofitSuccessCallback;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity {
    ActivityPlaceBinding b;
    Place data;
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
        retrievePlaceData(v -> {
            b.descrition.setText(v.getPlace().getDescription());
        });
    }

    private void retrievePlaceData(RetrofitSuccessCallback rsc){
        Networking.getInstance()
                .getJSONApi()
                .getPlace(getIntent().getIntExtra("place_id", 0))
                .enqueue(new Callback<CPlace>() {
                    @Override
                    public void onResponse(Call<CPlace> call, Response<CPlace> response) {
                        if (response.isSuccessful()){
                            //CPlace dat = response.body();
                            rsc.onSuccess(response.body());
                            Toast.makeText(getBaseContext(), response.body().getPlace().getDescription(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Invalid data "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CPlace> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}