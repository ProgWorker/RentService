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

import com.example.rentservice.databinding.FragmentRegistrationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {
    FragmentRegistrationBinding b;
    EditText log,pass,pass2;
    TextView link;
    Button login;

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
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        b.linkLog.setOnClickListener(v -> {
            goToLogin();
        });

        b.regBt.setOnClickListener(v -> {
            if(b.regPas1.getText().toString().equals(b.regPas2.getText().toString())) {
                SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("app.db", getActivity().MODE_PRIVATE, null);

                db.execSQL("CREATE TABLE IF NOT EXISTS Users (nick TEXT unique not null, pas TEXT, avatar TEXT , " +
                        "height INTEGER , weight INTEGER , blood_pressure TEXT , birth TEXT, phone Text)");
                //db.execSQL("DELETE FROM food WHERE animalType ="+ curentAnimal);

                Cursor query = db.rawQuery("SELECT * FROM Users WHERE nick like '" + b.regNick.getText() + "'" , null);
                if ((query != null) && (query.getCount() > 0)) {
                    Toast.makeText(getContext(), "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.execSQL("INSERT OR IGNORE INTO Users (nick, pas) VALUES ('" + b.regNick.getText().toString().replace("'", "''")
                            + "', '" + b.regPas1.getText().toString().replace("'", "''") + "')");

                    goToLogin();
                }
                db.close();
            } else {
                Toast.makeText(getContext(), "Пожалуйста, введите совпадающий пароль в оба поля", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}