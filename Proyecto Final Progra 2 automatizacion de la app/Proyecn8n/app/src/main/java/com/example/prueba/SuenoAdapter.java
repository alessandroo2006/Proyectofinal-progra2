package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuenoAdapter extends RecyclerView.Adapter<SuenoAdapter.SuenoViewHolder> {
    private List<Sueno> suenos;

    public SuenoAdapter(List<Sueno> suenos) {
        this.suenos = suenos;
    }

    @NonNull
    @Override
    public SuenoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new SuenoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuenoViewHolder holder, int position) {
        Sueno sueno = suenos.get(position);
        holder.textNombre.setText(sueno.getNombre());
        holder.textDetalle.setText(String.format("Objetivo: Q%.2f | Actual: Q%.2f | Prioridad: %s", 
            sueno.getMontoObjetivo(), sueno.getAhorroActual(), sueno.getPrioridad()));
    }

    @Override
    public int getItemCount() {
        return suenos.size();
    }

    static class SuenoViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        TextView textDetalle;

        public SuenoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(android.R.id.text1);
            textDetalle = itemView.findViewById(android.R.id.text2);
        }
    }
}
