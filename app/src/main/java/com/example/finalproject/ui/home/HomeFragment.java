package com.example.finalproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.api.ChuckNorrisApi;
import com.example.finalproject.api.ChuckNorrisQuote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private TextView textView;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textView = root.findViewById(R.id.chuck_norris_quote);
        imageView = root.findViewById(R.id.chuck_norris_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.chucknorris.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ChuckNorrisApi chuckNorrisApi = retrofit.create(ChuckNorrisApi.class);
                chuckNorrisApi.getQuote().enqueue(new Callback<ChuckNorrisQuote>() {
                    @Override
                    public void onResponse(Call<ChuckNorrisQuote> call, Response<ChuckNorrisQuote> response) {
                        if (response.code() == 200) {
                            textView.setText(response.body().getValue());
                        }
                    }

                    @Override
                    public void onFailure(Call<ChuckNorrisQuote> call, Throwable t) {
                        Log.i("Retrofit", "Something went wrong :(");
                    }
                });
            }
        });

        return root;
    }
}