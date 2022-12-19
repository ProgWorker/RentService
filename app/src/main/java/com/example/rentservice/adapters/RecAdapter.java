package com.example.rentservice.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.rentservice.HomeFragment;
import com.example.rentservice.R;
import com.example.rentservice.Server.POJO.Place.Address;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    private ArrayList<Place> data;
    GoToPlaceCallback cb;
    Context context;
    public RecAdapter(ArrayList<Place> data, Context context){
        this.data = data;
        this.context = context;
    }

    public void setGoPlace(GoToPlaceCallback cb){
        this.cb = cb;
    }

    public void setData(ArrayList<Place> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecAdapter.RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item, parent, false);
        return new RecAdapter.RecViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.RecViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        Address addr = data.get(position).getAddress();
        holder.subtitle.setText(addr.getCity() + ": " + addr.getStreet() + ", " + addr.getHome());

        Glide.with(context).load("http://10.0.2.2:8000"+data.get(position).getPict()).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            cb.goToPlace(data.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title, subtitle;
        Button exp;
        public RecViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.rec_image);
            this.title = itemView.findViewById(R.id.rec_header);
            this.subtitle = itemView.findViewById(R.id.rec_subtitle);
            this.exp = itemView.findViewById(R.id.rec_detail);
        }
    }
}