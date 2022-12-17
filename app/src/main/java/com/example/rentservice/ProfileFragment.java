package com.example.rentservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.util.*;
import com.example.rentservice.databinding.FragmentProfileBinding;

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
        b.login.setText(usr.getUsername());
        b.email.setText(usr.getEmail());
        b.name.setText(usr.getFirst_name());
        b.secondname.setText(usr.getLast_name());
        b.numberphone.setText(usr.getPhone());

        b.exit.setOnClickListener(v -> {
            helper.dropSession(usr.getId());
            Intent intent = new Intent(requireContext(), GreetingActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return b.getRoot();
    }
}