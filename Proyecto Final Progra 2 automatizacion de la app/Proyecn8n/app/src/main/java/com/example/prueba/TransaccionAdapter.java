package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {
    private List<Transaccion> transacciones;

    public TransaccionAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        holder.textDescripcion.setText(transaccion.getDescripcion());
        holder.textDetalle.setText(String.format("Q%.2f - %s (%s)", 
            transaccion.getMonto(), transaccion.getTipo(), transaccion.getFecha()));
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    static class TransaccionViewHolder extends RecyclerView.ViewHolder {
        TextView textDescripcion;
        TextView textDetalle;

        public TransaccionViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescripcion = itemView.findViewById(android.R.id.text1);
            textDetalle = itemView.findViewById(android.R.id.text2);
        }
    }
}
