package com.example.prueba;

public class AvatarMission {
    private int id;
    private String name;
    private String description;
    private double targetAmount;
    private String rewardType; // "hair", "clothing", "accessory", "background"
    private String rewardName;
    private String rewardDescription;
    private String rewardIcon;
    private boolean completed;
    private boolean claimed;

    public AvatarMission(int id, String name, String description, double targetAmount, 
                        String rewardType, String rewardName, String rewardDescription, String rewardIcon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.targetAmount = targetAmount;
        this.rewardType = rewardType;
        this.rewardName = rewardName;
        this.rewardDescription = rewardDescription;
        this.rewardIcon = rewardIcon;
        this.completed = false;
        this.claimed = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }

    public String getRewardName() { return rewardName; }
    public void setRewardName(String rewardName) { this.rewardName = rewardName; }

    public String getRewardDescription() { return rewardDescription; }
    public void setRewardDescription(String rewardDescription) { this.rewardDescription = rewardDescription; }

    public String getRewardIcon() { return rewardIcon; }
    public void setRewardIcon(String rewardIcon) { this.rewardIcon = rewardIcon; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isClaimed() { return claimed; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }
}

