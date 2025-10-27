package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DealAlertAdapter extends RecyclerView.Adapter<DealAlertAdapter.DealAlertViewHolder> {
    
    private List<DealAlert> dealAlerts = new ArrayList<>();
    private OnDealAlertClickListener listener;
    
    public interface OnDealAlertClickListener {
        void onDealAlertClick(DealAlert dealAlert);
        void onDealAlertLongClick(DealAlert dealAlert);
    }
    
    public DealAlertAdapter(OnDealAlertClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public DealAlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_deal_alert, parent, false);
        return new DealAlertViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DealAlertViewHolder holder, int position) {
        DealAlert dealAlert = dealAlerts.get(position);
        holder.bind(dealAlert);
    }
    
    @Override
    public int getItemCount() {
        return dealAlerts.size();
    }
    
    public void setDealAlerts(List<DealAlert> dealAlerts) {
        this.dealAlerts = dealAlerts;
        notifyDataSetChanged();
    }
    
    public void removeDealAlert(DealAlert dealAlert) {
        int position = dealAlerts.indexOf(dealAlert);
        if (position != -1) {
            dealAlerts.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    class DealAlertViewHolder extends RecyclerView.ViewHolder {
        private TextView txtKeywords;
        private TextView txtMaxPrice;
        private TextView txtSources;
        private TextView txtStatus;
        
        public DealAlertViewHolder(@NonNull View itemView) {
            super(itemView);
            txtKeywords = itemView.findViewById(R.id.txt_keywords);
            txtMaxPrice = itemView.findViewById(R.id.txt_max_price);
            txtSources = itemView.findViewById(R.id.txt_sources);
            txtStatus = itemView.findViewById(R.id.txt_status);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDealAlertClick(dealAlerts.get(position));
                    }
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDealAlertLongClick(dealAlerts.get(position));
                        return true;
                    }
                }
                return false;
            });
        }
        
        public void bind(DealAlert dealAlert) {
            txtKeywords.setText(dealAlert.getKeywords());
            txtMaxPrice.setText(dealAlert.getFormattedPrice());
            txtSources.setText(dealAlert.getSources());
            
            if (dealAlert.isActive()) {
                txtStatus.setText("Activa");
                txtStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                txtStatus.setText("Inactiva");
                txtStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}
