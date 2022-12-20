package com.example.rentservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.CPlace;
import com.example.rentservice.Server.POJO.Place.Cat;
import com.example.rentservice.Server.POJO.Place.Comment;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.Place.Room;
import com.example.rentservice.Server.POJO.User.UserData;
import com.example.rentservice.adapters.OrderAdapter;
import com.example.rentservice.databinding.ActivityPlaceBinding;
import com.example.rentservice.databinding.BookBinding;
import com.example.rentservice.util.SBHelper;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.example.rentservice.util.callbacks.GoToRoomCallback;
import com.example.rentservice.util.callbacks.RetrofitSuccessCallback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity {
    ActivityPlaceBinding b;
    Place data;
    CommentAdapter cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPlaceBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        SBHelper h = new SBHelper(this);
        UserData ud = h.getUserData();

        b.descrition.setMovementMethod(new ScrollingMovementMethod());
        b.orders.setLayoutManager(new LinearLayoutManager(this));
        RoomAdapter rd = new RoomAdapter();

            retrievePlaceData(v -> {
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load("http://10.0.2.2:8000"+v.getPlace().getPict()).resize(600,450).centerCrop().into(b.mainImage);
            b.recDetail.setOnClickListener(view -> {
                LayoutInflater m = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View vag = m.inflate(R.layout.comments, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(vag, width, height, focusable);
                RecyclerView av = vag.findViewById(R.id.comments);
                av.setLayoutManager(new LinearLayoutManager(this));
                av.setAdapter(cd);
                vag.findViewById(R.id.button).setOnClickListener(vi -> {
                    Toast.makeText(getApplicationContext(),"comment sended", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> body = new HashMap<>();
                    String msg = ((EditText)vag.findViewById(R.id.addcomments)).getText().toString();
                    body.put("msg", msg);
                    body.put("place_id", v.getPlace().getId());
                    Networking.getInstance().getJSONApi().postComment(ud.getToken(), body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            cd.data.add(new Comment(msg, ud.getUser().getId()));
                            cd.notifyItemInserted(cd.data.size()-1);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });
                });
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            });
            b.descrition.setText(v.getPlace().getDescription());
            b.appCompatAutoCompleteTextView.setText(v.getPlace().getTitle());
            rd.setData((ArrayList<Room>) v.getRoomsdata());
            if(v.getPlace().getUser().equalsIgnoreCase(String.valueOf(ud.getUser().getUsername()))){
                b.placeEdit.setOnClickListener(e -> {
                    Intent intent = new Intent(this, EditPlaceActivity.class);
                    intent.putExtra("title", v.getPlace().getTitle());
                    intent.putExtra("desc", v.getPlace().getDescription());
                    intent.putExtra("username", v.getPlace().getUser());
                    intent.putExtra("category", v.getPlace().getCategory());
                    intent.putExtra("country", v.getPlace().getAddress().getCountry());
                    intent.putExtra("city", v.getPlace().getAddress().getCity());
                    intent.putExtra("home", v.getPlace().getAddress().getHome());
                    intent.putExtra("street", v.getPlace().getAddress().getStreet());
                    intent.putExtra("region", v.getPlace().getAddress().getRegion());
                    intent.putExtra("place_id", v.getPlace().getId());
                    intent.putExtra("pict", v.getPlace().getPict());
                    startActivity(intent);
                    finish();
                });
                rd.setOnClick((c, t) -> {
                    LayoutInflater m = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View va = m.inflate(R.layout.ord_list, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(va, width, height, focusable);
                    RecyclerView av = va.findViewById(R.id.orders);
                    av.setLayoutManager(new LinearLayoutManager(this));
                    OrderAdapter od = new OrderAdapter(c);
                    od.setOwner(true);
                    av.setAdapter(od);
                    popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
                });
            } else {
                rd.setOnClick((c, t) -> {
                    BookBinding bb = BookBinding.inflate(getLayoutInflater());
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                    final PopupWindow popupWindow = new PopupWindow(bb.getRoot(), width, height, true);
                    bb.bookR.setOnClickListener(vc -> {
                        String dateFrom = bb.etDateFrom.getText().toString();
                        String dateTo = bb.etDateTo.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
                        SimpleDateFormat formatTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date date_from = new Date(), date_to = new Date();
                        try {
                            date_from = format.parse(dateFrom);
                            date_to = format.parse(dateTo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dateFrom = formatTo.format(date_from);
                        dateTo = formatTo.format(date_to);
                        bookPlace(new Order(dateFrom, dateTo, Integer.parseInt(bb.etPersons.getText().toString()), t), ud.getToken());
                        popupWindow.dismiss();
                    });
                    popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
                });
            }
            b.orders.setAdapter(rd);
            data = v.getPlace();
            cd = new CommentAdapter((ArrayList<Comment>) v.getComments());
        });

    }

    private void bookPlace(Order ord, String token){
        Networking.getInstance().getJSONApi().book(ord, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getBaseContext(), response.body(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {}
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

    static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

        private ArrayList<Room> data;
        GoToRoomCallback cd;
        boolean mode = false;

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

        public void setOnClick(GoToRoomCallback cd){
            this.cd=cd;
        }
        @NonNull
        @Override
        public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_node, parent, false);
            return new RoomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
            holder.status.setText(data.get(position).getStatus());
            holder.price.setText(String.valueOf(data.get(position).getCost()));
            holder.guests.setText("Гостей: "+data.get(position).getPersons());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            Date from, to;
            int cur = 0;
            for(Order o: data.get(position).getOrderdata()){
                try {
                    from = format.parse(o.getDate_from());
                    to = format.parse(o.getDate_to());
                    if(o.getStatus().equals("approved") & (from.before(today)& to.after(today)))
                        cur += o.getPersons();
                } catch (ParseException e) {
                    Log.e("DATE_ERR", "exception with date " + e.getMessage());
                }
            }
            holder.booked.setText("Забронировано: "+cur);
            holder.desc.setText(data.get(position).getInfo());
            holder.itemView.setOnClickListener(v -> {
                cd.goToRoom((ArrayList<Order>)data.get(position).getOrderdata(), data.get(position).getId());
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


    static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private ArrayList<Comment> data;

        public CommentAdapter(ArrayList<Comment> data){
            this.data = data;
        }

        public void setData(ArrayList<Comment> data) {
            this.data.clear();
            this.data.addAll(data);
            this.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_node, parent, false);
            return new CommentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            holder.textaq.setText(data.get(position).getDesc());
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {

            public TextView textaq;
            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                this.textaq = itemView.findViewById(R.id.com_text);
            }
        }
    }

}