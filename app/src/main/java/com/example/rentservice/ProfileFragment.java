package com.example.rentservice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Address;
import com.example.rentservice.Server.POJO.Place.Comment;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.adapters.OrderAdapter;
import com.example.rentservice.adapters.RecAdapter;
import com.example.rentservice.databinding.ActivityEditPlaceBinding;
import com.example.rentservice.databinding.FilterBinding;
import com.example.rentservice.util.*;
import com.example.rentservice.databinding.FragmentProfileBinding;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        b = FragmentProfileBinding.inflate(getLayoutInflater());
        SBHelper helper = new SBHelper(requireContext());
        UserData usrd = helper.getUserData();
        User usr = usrd.getUser();
        String fullPath = "http://10.0.2.2:8000"+usr.getAvatar();
        Picasso.get().load(fullPath).into(b.avatar);
        b.login.setText(usr.getUsername());
        b.email.setText(usr.getEmail());
        b.name.setText(usr.getFirst_name());
        b.secondname.setText(usr.getLast_name());
        b.numberphone.setText(usr.getPhone());
        b.editing.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileEditActivity.class));
        });
        b.exit.setOnClickListener(v -> {
            helper.dropSession(usr.getId());
            Intent intent = new Intent(requireContext(), GreetingActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        b.addPlace.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreatePlaceActivity.class);
            i.putExtra("username", usr.getUsername());
            i.putExtra("token", usrd.getToken());
            startActivity(i);
        });
        b.orders.setLayoutManager(new LinearLayoutManager(requireContext()));
        if(usr.getRole().equalsIgnoreCase("user")){
            Networking.getInstance().getJSONApi().getOrders(usrd.getToken()).enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    ArrayList<Order> a = (ArrayList<Order>) response.body();
                    OrderAdapter od = new OrderAdapter(a);
                    b.orders.setAdapter(od);

                }
                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {}
            });
        } else {
            Networking.getInstance().getJSONApi().getUPlaces(usrd.getToken()).enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    ArrayList<Place> a = (ArrayList<Place>) response.body();
                    RecAdapter rd = new RecAdapter(a);
                    rd.setGoPlace(v -> {
                        Intent i = new Intent(getActivity(), PlaceActivity.class);
                        i.putExtra("place_id", v);
                        startActivity(i);
                    });
                    b.orders.setAdapter(rd);
                }
                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {}
            });
        }
        return b.getRoot();
    }


}