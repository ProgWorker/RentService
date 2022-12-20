package com.example.rentservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.databinding.ActivityProfileEditBinding;
import com.example.rentservice.util.RealPathUtil;
import com.example.rentservice.util.SBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {
    ActivityProfileEditBinding b;
    Uri imgUri;
    String ava;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        SBHelper helper = new SBHelper(this);
        UserData usrd = helper.getUserData();
        b.email.setText(usrd.getUser().getEmail());
        b.name.setText(usrd.getUser().getFirst_name());
        b.secondname.setText(usrd.getUser().getLast_name());
        b.login.setText(usrd.getUser().getUsername());
        b.numberphone.setText(usrd.getUser().getPhone());
        Picasso.get().load("http://10.0.2.2:8000"+usrd.getUser().getAvatar()).into(b.avatar);
        ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                try {
                    if (result.getData() != null){
                        imgUri = result.getData().getData();
                        loadImage(usrd.getToken());
                    }
                }catch (Exception exception){
                    Log.d("TAG",""+exception.getLocalizedMessage());
                }
            }
        });
        b.avatar.setOnClickListener(v -> {
            pickImage.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        });
        b.save.setOnClickListener(v -> {
            User usr = usrd.getUser();
            if(imgUri!=null) {
                usr.setAvatar(ava);
            }
            usr.setEmail(Objects.requireNonNull(b.email.getText()).toString());
            usr.setFirst_name(Objects.requireNonNull(b.name.getText()).toString());
            usr.setLast_name(Objects.requireNonNull(b.secondname.getText()).toString());
            usr.setPhone(Objects.requireNonNull(b.numberphone.getText()).toString());
            usr.setUsername(Objects.requireNonNull(b.login.getText().toString()));
            if(b.newpass.getText().length()<1||b.newpasstwo.getText().length()<1)
                if(b.newpass.getText().toString().equals(b.newpasstwo.getText().toString())){

                }
            Networking.getInstance().getJSONApi().updateUser(usr, usrd.getToken()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    helper.dropSession(usr.getId());
                    helper.remeberUser(usr, usrd.getToken());
                    finish();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {}
            });
        });
    }
    void loadImage(String token){
        if(imgUri != null) {
            File file = new File(RealPathUtil.getPath(getBaseContext(), imgUri));
            RequestBody reqFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image",
                    file.getName(), reqFile);
            Networking.getInstance().getJSONApi().updateAvatar(token, body).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()){
                        Bitmap bitmap;
                        try {
                            ava = response.body();
                            bitmap = BitmapFactory.decodeStream(getBaseContext().getContentResolver().openInputStream(imgUri));
                            b.avatar.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    soutdata(t);
                }
            });
        }
    }
    void soutdata(@NonNull Throwable t){
        t.getCause();
    }

}