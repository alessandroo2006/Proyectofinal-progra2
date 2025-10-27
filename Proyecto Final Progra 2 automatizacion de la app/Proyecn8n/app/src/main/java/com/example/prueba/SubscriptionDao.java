package com.example.prueba;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubscriptionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Subscription subscription);
    
    @Update
    void update(Subscription subscription);
    
    @Delete
    void delete(Subscription subscription);
    
    @Query("SELECT * FROM subscriptions ORDER BY nextRenewalDate ASC")
    LiveData<List<Subscription>> getAllSubscriptions();
    
    @Query("SELECT * FROM subscriptions ORDER BY nextRenewalDate ASC")
    List<Subscription> getAllSubscriptionsSync();
    
    @Query("SELECT * FROM subscriptions WHERE id = :id")
    Subscription getSubscriptionById(int id);
    
    @Query("DELETE FROM subscriptions WHERE id = :id")
    void deleteById(int id);
    
    @Query("SELECT * FROM subscriptions WHERE nextRenewalDate BETWEEN :startDate AND :endDate")
    List<Subscription> getSubscriptionsByDateRange(long startDate, long endDate);
}
