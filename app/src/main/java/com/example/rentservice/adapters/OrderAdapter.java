package com.example.rentservice.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentservice.R;
import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> data;
    boolean owner = false;
    public OrderAdapter(ArrayList<Order> data){
        this.data = data;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setData(ArrayList<Order> data) {
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_node, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.status.setText(data.get(position).getStatus());
        holder.persons.setText(String.valueOf(data.get(position).getPersons()));
        holder.from.setText(data.get(position).getDate_from());
        holder.to.setText(data.get(position).getDate_to());
        holder.desc.setText(data.get(position).getP_name());
        if(owner){
            holder.itemView.setOnClickListener(v -> {
                if(!data.get(position).getStatus().equalsIgnoreCase("approved"))
                    Networking.getInstance().getJSONApi().approveOrder(data.get(position).getId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful())
                                holder.status.setText("approved");
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("DATAERRORZAPROS", t.getMessage());
                        }
                    });
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView status, persons, from, to, desc;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.status = itemView.findViewById(R.id.room_status);
            this.persons = itemView.findViewById(R.id.room_price);
            this.from = itemView.findViewById(R.id.room_guests);
            this.to = itemView.findViewById(R.id.room_booked);
            this.desc = itemView.findViewById(R.id.room_desc);
        }
    }
}
