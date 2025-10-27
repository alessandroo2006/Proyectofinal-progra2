package com.example.prueba;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SubscriptionRepository {
    private SubscriptionDao subscriptionDao;
    private LiveData<List<Subscription>> allSubscriptions;
    
    public SubscriptionRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        subscriptionDao = database.subscriptionDao();
        allSubscriptions = subscriptionDao.getAllSubscriptions();
    }
    
    public LiveData<List<Subscription>> getAllSubscriptions() {
        return allSubscriptions;
    }
    
    public List<Subscription> getAllSubscriptionsSync() {
        return subscriptionDao.getAllSubscriptionsSync();
    }
    
    public void insert(Subscription subscription) {
        new Thread(() -> subscriptionDao.insert(subscription)).start();
    }
    
    public void update(Subscription subscription) {
        new Thread(() -> subscriptionDao.update(subscription)).start();
    }
    
    public void delete(Subscription subscription) {
        new Thread(() -> subscriptionDao.delete(subscription)).start();
    }
    
    public void deleteById(int id) {
        new Thread(() -> subscriptionDao.deleteById(id)).start();
    }
    
    public Subscription getSubscriptionById(int id) {
        return subscriptionDao.getSubscriptionById(id);
    }
    
    public List<Subscription> getSubscriptionsByDateRange(long startDate, long endDate) {
        return subscriptionDao.getSubscriptionsByDateRange(startDate, endDate);
    }
}
