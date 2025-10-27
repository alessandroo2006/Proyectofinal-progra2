package com.example.prueba;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    
    public ExpenseRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }
    
    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }
    
    public List<Expense> getAllExpensesSync() {
        return expenseDao.getAllExpensesSync();
    }
    
    public void insert(Expense expense) {
        new Thread(() -> expenseDao.insert(expense)).start();
    }
    
    public void update(Expense expense) {
        new Thread(() -> expenseDao.update(expense)).start();
    }
    
    public void delete(Expense expense) {
        new Thread(() -> expenseDao.delete(expense)).start();
    }
    
    public void deleteById(int id) {
        new Thread(() -> expenseDao.deleteById(id)).start();
    }
    
    public Expense getExpenseById(int id) {
        return expenseDao.getExpenseById(id);
    }
    
    public LiveData<List<Expense>> getExpensesByUser(int userId) {
        return expenseDao.getExpensesByUser(userId);
    }
    
    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseDao.getExpensesByCategory(category);
    }
    
    public List<Expense> getExpensesByDateRange(long startDate, long endDate) {
        return expenseDao.getExpensesByDateRange(startDate, endDate);
    }
    
    public double getTotalExpensesByUser(int userId) {
        return expenseDao.getTotalExpensesByUser(userId);
    }
    
    public double getTotalExpensesByCategory(String category, int userId) {
        return expenseDao.getTotalExpensesByCategory(category, userId);
    }
    
    public double getTotalExpensesByDateRange(long startDate, long endDate, int userId) {
        return expenseDao.getTotalExpensesByDateRange(startDate, endDate, userId);
    }
}

