package com.house.keeper.data.database.dao

import androidx.room.*
import com.house.keeper.data.database.entities.TransactionEntity
import com.house.keeper.data.database.entities.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    
    @Query("SELECT * FROM transactions ORDER BY date DESC, createTime DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?
    
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountByTypeAndDateRange(type: TransactionType, startDate: Date, endDate: Date): Double?
    
    @Query("SELECT SUM(amount) FROM transactions WHERE categoryId = :categoryId AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountByCategoryAndDateRange(categoryId: Long, startDate: Date, endDate: Date): Double?
    
    @Query("SELECT * FROM transactions ORDER BY date DESC, createTime DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<TransactionEntity>>
    
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
    
    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    // Additional methods needed by repository
    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%Y-%m', date/1000, 'unixepoch') = strftime('%Y-%m', :month/1000, 'unixepoch')
        ORDER BY date DESC
    """)
    fun getTransactionsByMonth(month: Date): Flow<List<TransactionEntity>>

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'EXPENSE'
        AND strftime('%Y-%m', date/1000, 'unixepoch') = strftime('%Y-%m', :month/1000, 'unixepoch')
    """)
    suspend fun getMonthlyExpenseTotal(month: Date): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'INCOME'
        AND strftime('%Y-%m', date/1000, 'unixepoch') = strftime('%Y-%m', :month/1000, 'unixepoch')
    """)
    suspend fun getMonthlyIncomeTotal(month: Date): Double
}
