package com.house.keeper.data.repository

import com.house.keeper.data.model.Budget
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface BudgetRepository {
    
    fun getAllBudgets(): Flow<List<Budget>>
    
    suspend fun getBudgetById(id: Long): Budget?
    
    fun getBudgetsByCategory(categoryId: Long): Flow<List<Budget>>
    
    fun getBudgetsByDateRange(startDate: Date, endDate: Date): Flow<List<Budget>>
    
    fun getActiveBudgets(): Flow<List<Budget>>
    
    suspend fun insertBudget(budget: Budget): Long
    
    suspend fun updateBudget(budget: Budget)
    
    suspend fun deleteBudget(budget: Budget)
    
    suspend fun deleteBudgetById(id: Long)
    
    suspend fun getBudgetUsage(budgetId: Long): Double
    
    suspend fun isBudgetExceeded(budgetId: Long): Boolean
}
