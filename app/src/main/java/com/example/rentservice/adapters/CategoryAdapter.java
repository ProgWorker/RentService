package com.example.rentservice.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentservice.HomeFragment;
import com.example.rentservice.R;
import com.example.rentservice.Server.POJO.Place.Cat;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MoodViewHolder> {

    private ArrayList<Cat> data;
    private Context context;
    private MoodUpdateListener ntf;
    Bitmap bm;
    public CategoryAdapter(Context context, ArrayList<Cat> data, MoodUpdateListener nft){
        this.data = data;
        this.context=context;
        ntf=nft;
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgrounds);
    }

    public void setData(ArrayList<Cat> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_item, parent, false);
        return new MoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        holder.title.setText(data.get(position).getName());
        holder.category.setImageBitmap(bm);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MoodViewHolder extends RecyclerView.ViewHolder {

        public ImageButton category;
        public TextView title;
        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            this.category = itemView.findViewById(R.id.mood_icon);
            this.title = itemView.findViewById(R.id.mood_text);
        }
    }
}