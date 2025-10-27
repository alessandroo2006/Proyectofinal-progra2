package com.example.prueba;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }
    
    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }
    
    public LiveData<List<Expense>> getExpensesByUser(int userId) {
        return repository.getExpensesByUser(userId);
    }
    
    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(category);
    }
    
    public void insert(Expense expense) {
        try {
            repository.insert(expense);
        } catch (Exception e) {
            errorMessage.setValue("Error al guardar el gasto: " + e.getMessage());
        }
    }
    
    public void update(Expense expense) {
        try {
            repository.update(expense);
        } catch (Exception e) {
            errorMessage.setValue("Error al actualizar el gasto: " + e.getMessage());
        }
    }
    
    public void delete(Expense expense) {
        try {
            repository.delete(expense);
        } catch (Exception e) {
            errorMessage.setValue("Error al eliminar el gasto: " + e.getMessage());
        }
    }
    
    public void deleteById(int id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            errorMessage.setValue("Error al eliminar el gasto: " + e.getMessage());
        }
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}

