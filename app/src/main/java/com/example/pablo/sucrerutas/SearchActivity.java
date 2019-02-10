package com.example.pablo.sucrerutas;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    RecyclerView rvResultados;
    List<Linea> Lineas;
    TextView tvSinResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getParentActivityIntent());
            }
        });

        rvResultados = findViewById(R.id.rvResultados);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvResultados.setLayoutManager(llm);
        rvResultados.setHasFixedSize(true);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvResultados.addItemDecoration(itemDecor);

        tvSinResultados = findViewById(R.id.tvNoResults);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                findViewById(R.id.search_bar);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initializeData(query);
                initializeAdapter();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    rvResultados.setVisibility(View.VISIBLE);
                    initializeData(query);
                    initializeAdapter();
                }
                else{
                    rvResultados.setVisibility(View.GONE);
                    tvSinResultados.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
    }

    private void initializeData(String query){
        query = query.toLowerCase();
        Lineas = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("lineas.txt"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            String[] datos;

            while ((line = reader.readLine()) != null && !query.isEmpty()) {
                datos = line.split(",");
                if (datos[0].toLowerCase().contains(query))
                    Lineas.add(new Linea("Línea " + datos[0], datos[1]));
            }

            if (Lineas.size() == 0) {
                tvSinResultados.setVisibility(View.VISIBLE);
                Resources res = getResources();
                String mensaje =  String.format(res.getString(R.string.search_no_results), query);
                tvSinResultados.setText(mensaje);
            }
            else{
                tvSinResultados.setVisibility(View.GONE);
            }
        } catch (IOException e) {
            Toast.makeText(this, "revisa lineas.txt", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(Lineas);
        rvResultados.setAdapter(adapter);
    }
}