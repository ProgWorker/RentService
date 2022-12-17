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
        getActivity();
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = sp.getString("username", "default");
        if(user.equals("defaul")){
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
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

        final float scale = getContext().getResources().getDisplayMetrics().density;

        CategoryAdapter cd = new CategoryAdapter(getActivity(), mn, ()->{
            updateCategory(scale);
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

        RecAdapter rd = new RecAdapter(rn, scale, getContext());
        //ArrayList<RecNode> data = new ArrayList<>();
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.backgrounds);
        //data.add(new RecNode(bm, "asd", "asdf", "asdasf"));
        rd.setGoPlace(v -> {
            Intent i = new Intent(getActivity(), PlaceActivity.class);
            i.putExtra("place_id", v);
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




        //updateAdvise(scale);
        return root;
    }

    private void updateCategory(float scale){
        ArrayList<RecNode> rn = new ArrayList<>();
            //TODO: make load of predlozheniya here
        //b.recList.setAdapter(new RecAdapter(rn, scale));
        //((RecAdapter)b.recList.getAdapter()).setData(rn);
        //((RecAdapter)b.recList.getAdapter()).notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MoodViewHolder> {

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

    static class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

        private ArrayList<Place> data;
        float scale;
        GoToPlaceCallback cb;
        Bitmap bm;
        public RecAdapter(ArrayList<Place> data, float scale, Context context){
            this.data = data;
            this.scale = scale;
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgrounds);
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
        public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item, parent, false);
            return new RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
            holder.title.setText(data.get(position).getTitle());
            Address addr = data.get(position).getAddress();
            holder.subtitle.setText(addr.getCity() + ": " + addr.getStreet() + ", " + addr.getHome());
            holder.image.setImageBitmap(bm);
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
    public interface MoodUpdateListener{
        void onMoodUpdate();
    }
}