package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {
    
    private List<Subscription> subscriptions = new ArrayList<>();
    private OnSubscriptionClickListener listener;
    
    public interface OnSubscriptionClickListener {
        void onSubscriptionClick(Subscription subscription);
        void onSubscriptionLongClick(Subscription subscription);
    }
    
    public SubscriptionAdapter(OnSubscriptionClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subscription, parent, false);
        return new SubscriptionViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        Subscription subscription = subscriptions.get(position);
        holder.bind(subscription);
    }
    
    @Override
    public int getItemCount() {
        return subscriptions.size();
    }
    
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        notifyDataSetChanged();
    }
    
    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSubscriptionName;
        private TextView txtSubscriptionAmount;
        private TextView txtRenewalDate;
        private TextView txtDaysUntilRenewal;
        
        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubscriptionName = itemView.findViewById(R.id.txt_subscription_name);
            txtSubscriptionAmount = itemView.findViewById(R.id.txt_subscription_amount);
            txtRenewalDate = itemView.findViewById(R.id.txt_renewal_date);
            txtDaysUntilRenewal = itemView.findViewById(R.id.txt_days_until_renewal);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSubscriptionClick(subscriptions.get(position));
                    }
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSubscriptionLongClick(subscriptions.get(position));
                        return true;
                    }
                }
                return false;
            });
        }
        
        public void bind(Subscription subscription) {
            txtSubscriptionName.setText(subscription.getName());
            txtSubscriptionAmount.setText(subscription.getFormattedAmount());
            txtRenewalDate.setText(subscription.getFormattedRenewalDate());
            
            String daysText = subscription.getDaysUntilRenewalText();
            txtDaysUntilRenewal.setText(daysText);
            
            // Change color based on urgency
            if (subscription.getDaysUntilRenewal() <= 7) {
                txtDaysUntilRenewal.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else if (subscription.getDaysUntilRenewal() <= 30) {
                txtDaysUntilRenewal.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                txtDaysUntilRenewal.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            }
        }
    }
}
