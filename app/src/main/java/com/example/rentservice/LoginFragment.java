package com.example.rentservice;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.rentservice.Server.POJO.User.UserAuth;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.util.*;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    EditText log,pass;
    TextView link;
    Button login;


    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        log = view.findViewById(R.id.log_nick);
        pass = view.findViewById(R.id.log_pass);
        link = view.findViewById(R.id.link_reg);
        login = view.findViewById(R.id.login_bt);
        link.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .remove(this)
                    .add(R.id.fragment_container_view,new RegistrationFragment(),"TAG_1")
                    .commit();
        });
        /*Networking.getInstance()
                .getJSONApi()
                .getPostWithID(1)
                .enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                        Post post = response.body();

                        link.append(post.getId() + "\n");
                        link.append(post.getUserId() + "\n");
                        link.append(post.getTitle() + "\n");
                        link.append(post.getBody() + "\n");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {

                        link.append("Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });*/

        login.setOnClickListener(v -> {
            String nick = log.getText().toString();
            String pas = pass.getText().toString();
            Boolean success = false;
            Networking.getInstance()
                    .getJSONApi()
                    .authorize(new UserAuth(nick, pas))
                    .enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                            if (response.code() < 400){
                                UserData data = response.body();
                                new SBHelper(requireContext()).remeberUser(Objects.requireNonNull(data).getUser(), data.getToken());
                                Toast.makeText(getContext(), data.getUser().getUsername(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("username", data.getUser().getUsername());
                                intent.putExtra("userid", data.getUser().getId());
                                intent.putExtra("token", data.getToken());
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "Invalid data "+response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
                        }

                    });



        });
        return view;
    }
}