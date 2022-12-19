package com.example.rentservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Comment;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.adapters.OrderAdapter;
import com.example.rentservice.adapters.RecAdapter;
import com.example.rentservice.util.*;
import com.example.rentservice.databinding.FragmentProfileBinding;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                    RecAdapter rd = new RecAdapter(a, requireContext());
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