package com.example.rentservice;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Address;
import com.example.rentservice.Server.POJO.Place.CPlace;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.Place.Room;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.databinding.ActivityEditPlaceBinding;
import com.example.rentservice.databinding.RoomEditBinding;
import com.example.rentservice.util.RealPathUtil;
import com.example.rentservice.util.SBHelper;
import com.example.rentservice.util.callbacks.EditRoomCallback;
import com.example.rentservice.util.callbacks.GoToRoomCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPlaceActivity extends AppCompatActivity {
    ActivityEditPlaceBinding b;
    Uri imgUri;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityEditPlaceBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.etitle.setText(getIntent().getStringExtra("title"));
        b.edesc.setText(getIntent().getStringExtra("desc"));
        b.country.setText(getIntent().getStringExtra("country"));
        b.region.setText(getIntent().getStringExtra("region"));
        b.city.setText(getIntent().getStringExtra("city"));
        b.etitle.setText(getIntent().getStringExtra("street"));
        b.house.setText(getIntent().getStringExtra("home"));
        b.category.setText(getIntent().getStringExtra("category"));
        ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                try {
                    if (result.getData() != null){
                        imgUri = result.getData().getData();
                        loadImage();
                    }
                }catch (Exception exception){
                    Log.d("TAG",""+exception.getLocalizedMessage());
                }
            }
        });
        b.imageadd.setOnClickListener(v -> {
            pickImage.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        });
        RoomAdapter rd = new RoomAdapter();
        b.addRoom.setOnClickListener(v -> {
            RoomEditBinding rbb = RoomEditBinding.inflate(getLayoutInflater());
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(rbb.getRoot(), width, height, focusable);
            rbb.roomSave.setOnClickListener(vbb -> {
                try {
                    Room r = new Room(rbb.roomDesc.getText().toString(),
                            Integer.parseInt(rbb.roomGuests.getText().toString()),
                            Float.parseFloat(rbb.roomPrice.getText().toString()));
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("place_id", getIntent().getIntExtra("place_id",0));
                    hm.put("room_data", r);
                    Networking.getInstance().getJSONApi().updateRoom(hm).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            popupWindow.dismiss();
                            rd.data.add(r);
                            rd.notifyItemInserted(rd.data.size()-1);
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
        });
        b.save.setOnClickListener(v -> {
            place.setCategory(b.category.getText().toString());
            place.setDescription(b.etitle.getText().toString());
            place.setTitle(b.etitle.getText().toString());
            place.setAddress(new Address(b.country.getText().toString(),
                                         b.region.getText().toString(),
                                         b.city.getText().toString(),
                                         b.street.getText().toString(),
                                         b.house.getText().toString()));
            Networking.getInstance().getJSONApi().updatePlace(place).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    finish();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {}
            });
        });


        rd.setOnClick((v, i) -> {
            RoomEditBinding rbb = RoomEditBinding.inflate(getLayoutInflater());
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(rbb.getRoot(), width, height, focusable);
            rbb.roomDesc.setText(v.getInfo());
            rbb.roomGuests.setText(String.valueOf(v.getPersons()));
            rbb.roomPrice.setText(String.valueOf(v.getCost()));
            rbb.roomSave.setOnClickListener(vbb -> {
                try {
                    Room r = new Room(rbb.roomDesc.getText().toString(),
                            Integer.parseInt(rbb.roomGuests.getText().toString()),
                            Float.parseFloat(rbb.roomPrice.getText().toString()),
                            v.getId());
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("place_id", getIntent().getIntExtra("place_id",0));
                    hm.put("room_data", r);
                    Networking.getInstance().getJSONApi().updateRoom(hm).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            popupWindow.dismiss();
                            v.setCost(r.getCost());
                            v.setInfo(r.getInfo());
                            v.setPersons(r.getPersons());
                            rd.notifyItemChanged(i);
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
        });
        Networking.getInstance()
                .getJSONApi()
                .getPlace(getIntent().getIntExtra("place_id", 0))
                .enqueue(new Callback<CPlace>() {
                    @Override
                    public void onResponse(Call<CPlace> call, Response<CPlace> response) {
                        if (response.isSuccessful()){
                            place = response.body().getPlace();
                            rd.setData((ArrayList<Room>) response.body().getRoomsdata());
                            b.room.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                            b.room.setAdapter(rd);
                        }
                    }
                    @Override
                    public void onFailure(Call<CPlace> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Picasso.get().load("http://10.0.2.2:8000"+getIntent().getStringExtra("pict")).resize(1000,500).into(b.imageadd);

    }

    void loadImage(){
        if(imgUri != null) {
            File file = new File(RealPathUtil.getPath(getBaseContext(), imgUri));
            RequestBody reqFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image",
                    file.getName(), reqFile);
            Networking.getInstance().getJSONApi().updatePlaceAvatar(getIntent().getIntExtra("place_id",0), body).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()){
                        Bitmap bitmap;
                        try {
                            bitmap = BitmapFactory.decodeStream(getBaseContext().getContentResolver().openInputStream(imgUri));
                            b.imageadd.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d("AAAAAAAAAAAAAA", t.getMessage());
                }
            });
        }
    }

    static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

        private ArrayList<Room> data;
        EditRoomCallback cd;

        public RoomAdapter(ArrayList<Room> data){
            this.data = data;
        }

        public RoomAdapter(){
            this.data = new ArrayList<>();
        }

        public void setData(ArrayList<Room> data) {
            this.data.clear();
            this.data.addAll(data);
            this.notifyDataSetChanged();
        }
        public void setOnClick(EditRoomCallback cd){
            this.cd=cd;
        }
        @NonNull
        @Override
        public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_node, parent, false);
            return new RoomAdapter.RoomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
            holder.status.setText(data.get(position).getStatus());
            holder.price.setText(String.valueOf(data.get(position).getCost()));
            holder.guests.setText("Гостей: N");
            int cur = 0;
            holder.booked.setText("Забронировано: N");
            holder.desc.setText(data.get(position).getInfo());
            holder.itemView.setOnClickListener(v -> {
                cd.editRoom(data.get(position), position);

            });
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }

        static class RoomViewHolder extends RecyclerView.ViewHolder {

            public TextView status, price, guests, booked, desc;
            public RoomViewHolder(@NonNull View itemView) {
                super(itemView);
                this.status = itemView.findViewById(R.id.room_status);
                this.price = itemView.findViewById(R.id.room_price);
                this.guests = itemView.findViewById(R.id.room_guests);
                this.booked = itemView.findViewById(R.id.room_booked);
                this.desc = itemView.findViewById(R.id.room_desc);
            }
        }
    }
}