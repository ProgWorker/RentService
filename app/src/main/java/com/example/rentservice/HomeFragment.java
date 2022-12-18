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
import com.example.rentservice.adapters.RecAdapter;
import com.example.rentservice.util.callbacks.GoToPlaceCallback;
import com.example.rentservice.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;
    public String nik;
    private SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        View root = b.getRoot();
        nik = getActivity().getIntent().getStringExtra("Nickname");

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


        CategoryAdapter cd = new CategoryAdapter(getActivity(), mn, ()->{
            updateCategory();
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




        b.categoryList.setAdapter(cd);
        ArrayList<Place> rn = new ArrayList<>();



        b.filter.setOnClickListener(view -> {
            LayoutInflater m = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View v = m.inflate(R.layout.filter, null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(v, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        RecAdapter rd = new RecAdapter(rn, getContext());
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
                            rd.setData((ArrayList<Place>)data.getPlaces());
                        } else {
                            Toast.makeText(getContext(), "Invalid data "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PBase> call, Throwable t) {
                        Toast.makeText(requireContext(), "asdf", Toast.LENGTH_SHORT).show();
                    }

                });
        b.recList.setAdapter(rd);




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





}