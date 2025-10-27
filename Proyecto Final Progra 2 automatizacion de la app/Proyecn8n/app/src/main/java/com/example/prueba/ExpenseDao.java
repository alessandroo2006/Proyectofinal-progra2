package com.example.prueba;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    
    @Insert
    void insert(Expense expense);
    
    @Update
    void update(Expense expense);
    
    @Delete
    void delete(Expense expense);
    
    @Query("DELETE FROM expenses WHERE id = :id")
    void deleteById(int id);
    
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();
    
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpensesSync();
    
    @Query("SELECT * FROM expenses WHERE id = :id")
    Expense getExpenseById(int id);
    
    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByUser(int userId);
    
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByCategory(String category);
    
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<Expense> getExpensesByDateRange(long startDate, long endDate);
    
    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    double getTotalExpensesByUser(int userId);
    
    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category AND userId = :userId")
    double getTotalExpensesByCategory(String category, int userId);
    
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate AND userId = :userId")
    double getTotalExpensesByDateRange(long startDate, long endDate, int userId);
    
    @Query("DELETE FROM expenses")
    void deleteAll();
}

