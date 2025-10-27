package com.example.prueba;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class AhorroAvatarManager {
    private static final String PREF_NAME = "ahorro_avatar_prefs";
    private static final String KEY_TOTAL_SAVED = "total_saved";
    private static final String KEY_MISSION_COMPLETED_PREFIX = "mission_completed_";
    private static final String KEY_MISSION_CLAIMED_PREFIX = "mission_claimed_";
    private static final String KEY_EQUIPPED_ITEMS = "equipped_items";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private List<AvatarMission> missions;
    
    public AhorroAvatarManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        initializeMissions();
    }
    
    private void initializeMissions() {
        missions = new ArrayList<>();
        
        // Misiones predefinidas
        missions.add(new AvatarMission(1, "Nuevo Peinado", "Ahorra para un peinado genial", 50.0, 
            "hair", "Peinado Estilo", "Un peinado moderno y cool", "💇‍♂️"));
        
        missions.add(new AvatarMission(2, "Camiseta Trendy", "Consigue esa camiseta que querías", 100.0, 
            "clothing", "Camiseta Estilo", "Una camiseta súper trendy", "👕"));
        
        missions.add(new AvatarMission(3, "Accesorio Especial", "Un accesorio que te haga único", 200.0, 
            "accessory", "Collar Dorado", "Un collar que brilla como el oro", "📿"));
        
        missions.add(new AvatarMission(4, "Fondo Épico", "Un fondo para tu avatar", 300.0, 
            "background", "Fondo Espacial", "Viaja por las estrellas", "🌌"));
        
        missions.add(new AvatarMission(5, "Zapatos Geniales", "Los zapatos más cool", 150.0, 
            "clothing", "Tenis Estilo", "Los tenis más geniales", "👟"));
        
        missions.add(new AvatarMission(6, "Gafas de Sol", "Para verte súper cool", 80.0, 
            "accessory", "Gafas Cool", "Gafas que te hacen ver genial", "🕶️"));
        
        // Cargar estado de las misiones
        loadMissionStates();
    }
    
    private void loadMissionStates() {
        for (AvatarMission mission : missions) {
            boolean completed = prefs.getBoolean(KEY_MISSION_COMPLETED_PREFIX + mission.getId(), false);
            boolean claimed = prefs.getBoolean(KEY_MISSION_CLAIMED_PREFIX + mission.getId(), false);
            mission.setCompleted(completed);
            mission.setClaimed(claimed);
        }
    }
    
    public void addSavings(double amount) {
        double currentTotal = getTotalSaved();
        double newTotal = currentTotal + amount;
        setTotalSaved(newTotal);
        
        // Verificar si se completó alguna misión
        checkMissionsCompletion();
    }
    
    private void checkMissionsCompletion() {
        double totalSaved = getTotalSaved();
        
        for (AvatarMission mission : missions) {
            if (!mission.isCompleted() && totalSaved >= mission.getTargetAmount()) {
                mission.setCompleted(true);
                editor.putBoolean(KEY_MISSION_COMPLETED_PREFIX + mission.getId(), true);
                editor.apply();
            }
        }
    }
    
    public double getTotalSaved() {
        return Double.longBitsToDouble(prefs.getLong(KEY_TOTAL_SAVED, 0L));
    }
    
    private void setTotalSaved(double amount) {
        editor.putLong(KEY_TOTAL_SAVED, Double.doubleToRawLongBits(amount));
        editor.apply();
    }
    
    public List<AvatarMission> getMissions() {
        return missions;
    }
    
    public List<AvatarMission> getAvailableMissions() {
        List<AvatarMission> available = new ArrayList<>();
        double totalSaved = getTotalSaved();
        
        for (AvatarMission mission : missions) {
            if (totalSaved >= mission.getTargetAmount() && !mission.isClaimed()) {
                available.add(mission);
            }
        }
        return available;
    }
    
    public AvatarMission getNextMission() {
        double totalSaved = getTotalSaved();
        
        for (AvatarMission mission : missions) {
            if (totalSaved < mission.getTargetAmount()) {
                return mission;
            }
        }
        return null; // Todas las misiones completadas
    }
    
    public void claimReward(int missionId) {
        for (AvatarMission mission : missions) {
            if (mission.getId() == missionId && mission.isCompleted() && !mission.isClaimed()) {
                mission.setClaimed(true);
                editor.putBoolean(KEY_MISSION_CLAIMED_PREFIX + missionId, true);
                editor.apply();
                break;
            }
        }
    }
    
    public List<AvatarMission> getClaimedRewards() {
        List<AvatarMission> claimed = new ArrayList<>();
        for (AvatarMission mission : missions) {
            if (mission.isClaimed()) {
                claimed.add(mission);
            }
        }
        return claimed;
    }
    
    public String getMotivationalMessage() {
        double totalSaved = getTotalSaved();
        AvatarMission nextMission = getNextMission();
        
        if (nextMission == null) {
            return "¡Increíble! 🎉 Has completado todas las misiones. ¡Eres un maestro del ahorro! 💰✨";
        }
        
        double remaining = nextMission.getTargetAmount() - totalSaved;
        
        if (remaining <= 0) {
            return "¡Felicidades! 🎊 Has completado la misión '" + nextMission.getName() + "'! 🎁";
        } else if (remaining <= 10) {
            return "¡Estás súper cerca! 🔥 Solo te faltan Q" + String.format("%.0f", remaining) + 
                   " para '" + nextMission.getName() + "'! 💪";
        } else if (remaining <= 25) {
            return "¡Vas excelente! ⭐ Te faltan Q" + String.format("%.0f", remaining) + 
                   " para '" + nextMission.getName() + "'! 🚀";
        } else {
            return "¡Genial progreso! 💰 Llevas Q" + String.format("%.0f", totalSaved) + 
                   " ahorrados. Sigue así! 🎯";
        }
    }
}

