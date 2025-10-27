package com.example.prueba;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class SubscriptionViewModel extends AndroidViewModel {
    private SubscriptionRepository repository;
    private LiveData<List<Subscription>> allSubscriptions;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public SubscriptionViewModel(Application application) {
        super(application);
        repository = new SubscriptionRepository(application);
        allSubscriptions = repository.getAllSubscriptions();
    }
    
    public LiveData<List<Subscription>> getAllSubscriptions() {
        return allSubscriptions;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void insert(Subscription subscription) {
        try {
            repository.insert(subscription);
        } catch (Exception e) {
            errorMessage.setValue("Error al guardar la suscripción: " + e.getMessage());
        }
    }
    
    public void update(Subscription subscription) {
        try {
            repository.update(subscription);
        } catch (Exception e) {
            errorMessage.setValue("Error al actualizar la suscripción: " + e.getMessage());
        }
    }
    
    public void delete(Subscription subscription) {
        try {
            repository.delete(subscription);
        } catch (Exception e) {
            errorMessage.setValue("Error al eliminar la suscripción: " + e.getMessage());
        }
    }
    
    public void deleteById(int id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            errorMessage.setValue("Error al eliminar la suscripción: " + e.getMessage());
        }
    }
    
    public List<Subscription> getAllSubscriptionsSync() {
        return repository.getAllSubscriptionsSync();
    }
    
    public List<Subscription> getSubscriptionsByDateRange(long startDate, long endDate) {
        return repository.getSubscriptionsByDateRange(startDate, endDate);
    }
}
