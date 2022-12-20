package com.example.rentservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Address;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.databinding.ActivityEditPlaceBinding;
import com.example.rentservice.util.RealPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePlaceActivity extends AppCompatActivity {
    ActivityEditPlaceBinding b;
    Uri imgUri;
    ActivityResultLauncher<Intent> pickImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityEditPlaceBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        b.room.setVisibility(View.INVISIBLE);
        b.addRoom.setVisibility(View.INVISIBLE);
        b.roomheader.setVisibility(View.INVISIBLE);
        pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                try {
                    if (result.getData() != null){
                        imgUri = result.getData().getData();
                        b.imageadd.setImageBitmap(BitmapFactory.decodeStream(getBaseContext().getContentResolver().openInputStream(imgUri)));
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            fb.imageadd.setImageBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), imgUri)));
                        }*/
                    }
                }catch (Exception exception){
                    Log.d("TAG",""+exception.getLocalizedMessage());
                }
            }
        });
        b.imageadd.setOnClickListener(y -> {
            pickImage.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        });
        b.save.setOnClickListener(g -> {

            String title, desc, category, country, region, city, street, home;
            title = TextUtils.isEmpty(b.etitle.getText())?"":b.etitle.getText().toString();
            desc = TextUtils.isEmpty(b.edesc.getText())?"":b.edesc.getText().toString();
            category = TextUtils.isEmpty(b.category.getText())?"":b.category.getText().toString();
            country = TextUtils.isEmpty(b.country.getText())?"":b.country.getText().toString();
            region = TextUtils.isEmpty(b.region.getText())?"":b.region.getText().toString();
            city = TextUtils.isEmpty(b.city.getText())?"":b.city.getText().toString();
            street = TextUtils.isEmpty(b.street.getText())?"":b.street.getText().toString();
            home = TextUtils.isEmpty(b.house.getText())?"":b.house.getText().toString();

            if(TextUtils.isEmpty(b.etitle.getText()))
                Toast.makeText(this, "Установите название объявления", Toast.LENGTH_LONG).show();
            else if(country.equals("")||region.equals("")||
                    city.equals("")||street.equals("")||home.equals(""))
                Toast.makeText(this, "Установите адрес", Toast.LENGTH_LONG).show();
            else if(category.equals(""))
                Toast.makeText(this, "Установите категорию", Toast.LENGTH_LONG).show();
            else if(b.imageadd.getDrawable() == null)
                Toast.makeText(this, "Установите изображение объявления", Toast.LENGTH_LONG).show();
            else {
                File file = new File(RealPathUtil.getPath(this, imgUri));
                RequestBody reqFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image",
                        file.getName(), reqFile);
                Place place = new Place(title, desc, getIntent().getStringExtra("username"), category);
                place.setAddress(new Address(country, region, city, street, home));
                Networking.getInstance().getJSONApi().createPlace(place, body, getIntent().getStringExtra("token")).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            }
        });
    }
}