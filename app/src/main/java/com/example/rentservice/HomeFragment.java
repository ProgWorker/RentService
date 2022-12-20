package com.example.rentservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentservice.Server.Networking;
import com.example.rentservice.Server.POJO.Place.Address;
import com.example.rentservice.Server.POJO.Place.Cat;
import com.example.rentservice.Server.POJO.Place.Cats;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.adapters.CategoryAdapter;
import com.example.rentservice.adapters.OrderAdapter;
import com.example.rentservice.adapters.RecAdapter;
import com.example.rentservice.databinding.FilterBinding;
import com.example.rentservice.util.callbacks.ChangeCategoryCallback;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.example.rentservice.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;
    public String nik;
    private SharedPreferences sp;
    ArrayList<String> params;
    ArrayList<Place> places;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        View root = b.getRoot();
        places = new ArrayList<>();
        nik = getActivity().getIntent().getStringExtra("Nickname");
        params = new ArrayList<>();
        LinearLayoutManager category_manager = new LinearLayoutManager(getContext());
        category_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        b.categoryList.setLayoutManager(category_manager);
        b.recList.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Cat> mn = new ArrayList<>();

        /*AssetManager assetManager = getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("books/IndividualVillas/images_bali/1.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);*/


        CategoryAdapter cd = new CategoryAdapter(getActivity(), mn);
        Networking.getInstance().getJSONApi().getParams().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful())
                    params.addAll(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {}
        });
        Networking.getInstance()
                .getJSONApi()
                .getCategories()
                .enqueue(new Callback<Cats>() {
                    @Override
                    public void onResponse(Call<Cats> call, Response<Cats> response) {
                        if (response.code() < 400){
                            Cats data = response.body();
                            cd.setData((ArrayList<Cat>)data.getCats());
                        } else {
                            Toast.makeText(getContext(), "Invalid data "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cats> call, Throwable t) {}

                });



        RecAdapter rd = new RecAdapter(places);

        cd.setFilter(v -> {
            rd.setData((ArrayList<Place>) places.stream().filter(c -> c.getCategory().equals(v)).collect(Collectors.toList()));
            rd.notifyDataSetChanged();
        });

        b.categoryList.setAdapter(cd);






        //ArrayList<RecNode> data = new ArrayList<>();
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.backgrounds);
        //data.add(new RecNode(bm, "asd", "asdf", "asdasf"));
        rd.setGoPlace(v -> {
            Intent i = new Intent(getActivity(), PlaceActivity.class);
            i.putExtra("place_id", v);
            i.putExtra("username", nik);
            startActivity(i);
        });
        //rd.setData(data);
        Networking.getInstance()
                .getJSONApi()
                .getPlaces()
                .enqueue(new Callback<PBase>() {
                    @Override
                    public void onResponse(Call<PBase> call, Response<PBase> response) {
                        if (response.code() < 400){
                            PBase data = response.body();
                            places = (ArrayList<Place>)data.getPlaces();
                            rd.setData((ArrayList<Place>)data.getPlaces());
                        } else {
                            Toast.makeText(requireContext(), "Invalid data "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<PBase> call, Throwable t) {
                        Toast.makeText(requireContext(), "asdf", Toast.LENGTH_SHORT).show();
                    }
                });
        b.recList.setAdapter(rd);

        b.filter.setOnClickListener(view -> {
            FilterBinding fb = FilterBinding.inflate(getLayoutInflater());
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            final PopupWindow popupWindow = new PopupWindow(fb.getRoot(), width, height, true);
            ParamAdapter pd = new ParamAdapter(params);
            fb.filterview.setLayoutManager(new LinearLayoutManager(getContext()));
            fb.filterview.setAdapter(pd);
            fb.button.setOnClickListener(v -> {
                Map<String,String> m = new HashMap<>();
                if(fb.city.getText().length()>0)
                    m.put("city", fb.city.getText().toString());
                if(fb.country.getText().length()>0)
                    m.put("country", fb.country.getText().toString());
                if(fb.home.getText().length()>0)
                    m.put("home", fb.home.getText().toString());
                if(fb.title.getText().length()>0)
                    m.put("title", fb.title.getText().toString());
                m.put("rate", String.valueOf(fb.seekBar.getProgress()));
                Networking.getInstance().getJSONApi().filterPlaces(m, pd.getBody()).enqueue(new Callback<ArrayList<Place>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Place>> call, Response<ArrayList<Place>> response) {
                        if(response.isSuccessful()){
                            ArrayList<Place> data = response.body();
                            rd.setData(data);
                            popupWindow.dismiss();
                        }
                        Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Place>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            });
            popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
        });


        //updateAdvise();
        return root;
    }

    private void updateCategory(){
        ArrayList<RecNode> rn = new ArrayList<>();
            //TODO: make load of predlozheniya here
        //b.recList.setAdapter(new RecAdapter(rn));
        //((RecAdapter)b.recList.getAdapter()).setData(rn);
        //((RecAdapter)b.recList.getAdapter()).notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }



    class ParamAdapter extends RecyclerView.Adapter<ParamAdapter.ParamVH> {
        ArrayList<String> data;
        ArrayList<String> body;
        public ParamAdapter(ArrayList<String> data){
            this.data=data;
            this.body = new ArrayList<>();
        }

        public void setData(ArrayList<String> data) {
            this.data = data;
        }

        public ArrayList<String> getBody() {
            return body;
        }

        @NonNull
        @Override
        public ParamVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cd_node, parent, false);
            return new ParamVH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ParamVH holder, int position) {
            holder.cb.setText(data.get(position));
            holder.cb.setOnCheckedChangeListener((v, c) -> {
                if(c){
                    body.add(data.get(position));
                } else {
                    body.remove(data.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ParamVH extends RecyclerView.ViewHolder{
            CheckBox cb;
            public ParamVH(@NonNull View itemView) {
                super(itemView);
                cb=itemView.findViewById(R.id.cd_node);
            }
        }
    }

}