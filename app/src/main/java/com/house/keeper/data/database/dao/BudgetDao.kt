package com.house.keeper.data.database.dao

import androidx.room.*
import com.house.keeper.data.database.entities.BudgetEntity
import com.house.keeper.data.database.entities.BudgetPeriod
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BudgetDao {
    
    @Query("SELECT * FROM budgets ORDER BY createTime DESC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetEntity?
    
    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId")
    fun getBudgetsByCategory(categoryId: Long): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE period = :period ORDER BY createTime DESC")
    fun getBudgetsByPeriod(period: BudgetPeriod): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE startDate <= :date AND endDate >= :date")
    fun getActiveBudgets(date: Date): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND startDate <= :date AND endDate >= :date")
    suspend fun getActiveBudgetByCategory(categoryId: Long, date: Date): BudgetEntity?
    
    @Insert
    suspend fun insertBudget(budget: BudgetEntity): Long
    
    @Update
    suspend fun updateBudget(budget: BudgetEntity)
    
    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
    
    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudgetById(id: Long)
    
    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    // Additional methods needed by repository
    @Query("SELECT * FROM budgets WHERE startDate <= :endDate AND endDate >= :startDate")
    fun getBudgetsByDateRange(startDate: Date, endDate: Date): Flow<List<BudgetEntity>>

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0) as used
        FROM transactions t
        WHERE t.categoryId = :categoryId
        AND t.date BETWEEN :startDate AND :endDate
        AND t.type = 'EXPENSE'
    """)
    suspend fun getBudgetUsage(categoryId: Long, startDate: Date, endDate: Date): Double

    @Query("""
        SELECT CASE
            WHEN (SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                  WHERE t.categoryId = :categoryId
                  AND t.date BETWEEN :startDate AND :endDate
                  AND t.type = 'EXPENSE') > :budgetAmount
            THEN 1
            ELSE 0
        END
    """)
    suspend fun isBudgetExceeded(categoryId: Long, startDate: Date, endDate: Date, budgetAmount: Double): Boolean
}
