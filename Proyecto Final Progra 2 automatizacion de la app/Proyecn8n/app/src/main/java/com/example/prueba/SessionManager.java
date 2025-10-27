package com.example.prueba;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_LAST_ACTIVITY = "last_activity";
    private static final String KEY_SESSION_TIMEOUT = "session_timeout";
    
    // 15 minutos en milisegundos
    private static final long SESSION_TIMEOUT_DURATION = 15 * 60 * 1000;
    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Handler sessionHandler;
    private Runnable sessionTimeoutRunnable;
    
    public interface SessionTimeoutCallback {
        void onSessionTimeout();
    }
    
    private SessionTimeoutCallback timeoutCallback;
    
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        sessionHandler = new Handler(Looper.getMainLooper());
    }
    
    public void setSessionTimeoutCallback(SessionTimeoutCallback callback) {
        this.timeoutCallback = callback;
    }
    
    public void createLoginSession(int userId, String username, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putLong(KEY_LAST_ACTIVITY, System.currentTimeMillis());
        editor.putLong(KEY_SESSION_TIMEOUT, System.currentTimeMillis() + SESSION_TIMEOUT_DURATION);
        editor.commit();
        
        startSessionTimeout();
    }
    
    public void updateLastActivity() {
        editor.putLong(KEY_LAST_ACTIVITY, System.currentTimeMillis());
        editor.putLong(KEY_SESSION_TIMEOUT, System.currentTimeMillis() + SESSION_TIMEOUT_DURATION);
        editor.commit();
        
        // Reiniciar el timeout
        startSessionTimeout();
    }
    
    public boolean isLoggedIn() {
        if (!pref.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return false;
        }
        
        // Verificar si la sesión ha expirado
        long sessionTimeout = pref.getLong(KEY_SESSION_TIMEOUT, 0);
        if (System.currentTimeMillis() > sessionTimeout) {
            logoutUser();
            return false;
        }
        
        return true;
    }
    
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }
    
    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }
    
    public String getCurrentUsername() {
        return pref.getString(KEY_USERNAME, null);
    }
    
    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }
    
    public long getLastActivity() {
        return pref.getLong(KEY_LAST_ACTIVITY, 0);
    }
    
    public long getSessionTimeout() {
        return pref.getLong(KEY_SESSION_TIMEOUT, 0);
    }
    
    public long getRemainingSessionTime() {
        long timeout = getSessionTimeout();
        long remaining = timeout - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
    
    public void logoutUser() {
        editor.clear();
        editor.commit();
        stopSessionTimeout();
    }
    
    public void logout() {
        logoutUser();
    }
    
    private void startSessionTimeout() {
        stopSessionTimeout(); // Cancelar timeout anterior si existe
        
        sessionTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeoutCallback != null) {
                    timeoutCallback.onSessionTimeout();
                }
                logoutUser();
            }
        };
        
        sessionHandler.postDelayed(sessionTimeoutRunnable, SESSION_TIMEOUT_DURATION);
    }
    
    private void stopSessionTimeout() {
        if (sessionTimeoutRunnable != null) {
            sessionHandler.removeCallbacks(sessionTimeoutRunnable);
            sessionTimeoutRunnable = null;
        }
    }
    
    public void onDestroy() {
        stopSessionTimeout();
    }
}
