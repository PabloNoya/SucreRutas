package com.example.pablo.sucrerutas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Linea> Lineas;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Divisor
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(itemDecor);

        initializeData();
        initializeAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);
                return true;

            case R.id.action_about:
                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ArrayList de Lineas
    private void initializeData(){
        Lineas = new ArrayList<>();
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open("lineas.txt"));
            BufferedReader reader = new BufferedReader(is);
            String line;
            String[] st;

            while ((line = reader.readLine()) != null){
                st = line.split(",");
                Lineas.add(new Linea("Línea " + st[0], st[1]));
            }
        }
        catch (IOException e){
            Lineas.add(new Linea("FALLÓ", "verifica el archivo assets/lineas.txt"));
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(Lineas);
        rv.setAdapter(adapter);
    }
}
