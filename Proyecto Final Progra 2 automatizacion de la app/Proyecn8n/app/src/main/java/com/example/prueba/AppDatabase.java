package com.example.prueba;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Subscription.class, Expense.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase INSTANCE;
    
    public abstract UserDao userDao();
    public abstract SubscriptionDao subscriptionDao();
    public abstract ExpenseDao expenseDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "app_database"
            ).allowMainThreadQueries() // Para simplificar, permitimos queries en el hilo principal
             .fallbackToDestructiveMigration() // Para manejar cambios de esquema
             .build();
        }
        return INSTANCE;
    }
}
