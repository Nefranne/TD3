package com.example.td3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    static final String BASE_URL = "https://pokeapi.co/";
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

       // showList();
        makeApiCall();

    }

    private void showList(List<Pokemon> pokemonList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        mAdapter = new ListAdapter(pokemonList);
        recyclerView.setAdapter(mAdapter);
    }



        private void makeApiCall () {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            PokeApi pokeApi = retrofit.create(PokeApi.class);

            Call<RestPokemonResponse> call = pokeApi.getPokemonResponse();
            call.enqueue(new Callback<RestPokemonResponse>() {
                @Override
                public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Pokemon> pokemonList = response.body().getResults();
                        showSucces();
                        showList(pokemonList);
                        //Toast.makeText(getApplicationContext(), "API success", Toast.LENGTH_SHORT).show();

                    }
                    else showError();
                }

                @Override
                public void onFailure(Call<RestPokemonResponse> call, Throwable t) {
                    showError();

                }
            });
        }

    private void showSucces() {
        Toast.makeText(this, "API success", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(this, "API Error", Toast.LENGTH_SHORT).show();
    }


}
