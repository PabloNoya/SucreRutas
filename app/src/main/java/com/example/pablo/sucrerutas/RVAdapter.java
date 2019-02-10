package com.example.pablo.sucrerutas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LineaViewHolder>{
    public static class LineaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView LineaNombre;
        TextView LineaDestinos;
        Context context;

        LineaViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            itemView.setOnClickListener(this);

            LineaNombre = itemView.findViewById(R.id.LineaNombre);
            LineaDestinos = itemView.findViewById(R.id.LineaDestino);
        }

        @Override
        public void onClick(View view){
            Intent mapa = new Intent(context, MapsActivity.class);
            String nombre = (String) LineaNombre.getText();
            mapa.putExtra("nombreLinea", nombre.replace("Línea ", ""));
            context.startActivity(mapa);
        }
    }

    // Datos
    private List<Linea> Lineas;
    RVAdapter(List<Linea> Lineas){
        this.Lineas = Lineas;
    }

    @NonNull
    @Override
    public LineaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linea, viewGroup, false);
        return new LineaViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull LineaViewHolder lineaViewHolder, int i) {
        lineaViewHolder.LineaNombre.setText(Lineas.get(i).nombre);
        lineaViewHolder.LineaDestinos.setText(Lineas.get(i).destinos);
    }

    @Override
    public int getItemCount() {
        return Lineas.size();
    }
}
