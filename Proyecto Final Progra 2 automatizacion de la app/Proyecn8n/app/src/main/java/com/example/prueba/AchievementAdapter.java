package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    
    private List<Achievement> achievements = new ArrayList<>();
    private OnAchievementClickListener listener;
    
    public interface OnAchievementClickListener {
        void onAchievementClick(Achievement achievement);
    }
    
    public AchievementAdapter(OnAchievementClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_achievement, parent, false);
        return new AchievementViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.bind(achievement);
    }
    
    @Override
    public int getItemCount() {
        return achievements.size();
    }
    
    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
        notifyDataSetChanged();
    }
    
    class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAchievementIcon;
        private TextView txtAchievementName;
        private TextView txtAchievementDescription;
        
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAchievementIcon = itemView.findViewById(R.id.txt_achievement_icon);
            txtAchievementName = itemView.findViewById(R.id.txt_achievement_name);
            txtAchievementDescription = itemView.findViewById(R.id.txt_achievement_description);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAchievementClick(achievements.get(position));
                    }
                }
            });
        }
        
        public void bind(Achievement achievement) {
            txtAchievementIcon.setText(achievement.getIcon());
            txtAchievementName.setText(achievement.getName());
            txtAchievementDescription.setText(achievement.getDescription());
            
            if (achievement.isUnlocked()) {
                // Unlocked achievement - full color
                txtAchievementIcon.setAlpha(1.0f);
                txtAchievementName.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                txtAchievementDescription.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            } else {
                // Locked achievement - grayed out
                txtAchievementIcon.setAlpha(0.5f);
                txtAchievementName.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
                txtAchievementDescription.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
}
