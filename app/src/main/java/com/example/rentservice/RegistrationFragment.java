package com.example.rentservice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.databinding.FragmentRegistrationBinding;
import com.example.rentservice.util.SBHelper;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {
    FragmentRegistrationBinding b;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = FragmentRegistrationBinding.inflate(getLayoutInflater());
    }

    private void goToLogin(){
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction()
                .remove(this)
                .add(R.id.fragment_container_view,new LoginFragment(),"TAG_2")
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentRegistrationBinding.inflate(getLayoutInflater());
        b.linkLog.setOnClickListener(v -> {
            goToLogin();
        });

        b.regBt.setOnClickListener(v -> {
            boolean can = !b.phone.getText().toString().isEmpty() &&
                    !b.email.getText().toString().isEmpty() &&
                    !b.regNick.getText().toString().isEmpty() &&
                    !b.regPas1.getText().toString().isEmpty() &&
                    !b.regPas2.getText().toString().isEmpty();
            if(b.regPas1.getText().toString().equals(b.regPas2.getText().toString()) && can) {
                User user = new User(b.regNick.getText().toString(), b.email.getText().toString(),
                        b.phone.getText().toString(), b.cdNode.isChecked()?"Owner":"User", b.regPas1.getText().toString());
                Networking.getInstance().getJSONApi().createUser(user).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        UserData data = response.body();
                        new SBHelper(requireContext()).remeberUser(Objects.requireNonNull(data).getUser(), data.getToken());
                        Toast.makeText(getContext(), data.getUser().getUsername(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("username", data.getUser().getUsername());
                        intent.putExtra("userid", data.getUser().getId());
                        intent.putExtra("token", data.getToken());
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return b.getRoot();
    }
}