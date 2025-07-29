package com.house.keeper.data.repository

import com.house.keeper.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    
    fun getAllTransactions(): Flow<List<Transaction>>
    
    suspend fun getTransactionById(id: Long): Transaction?
    
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>
    
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>>
    
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>
    
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>>
    
    suspend fun insertTransaction(transaction: Transaction): Long
    
    suspend fun updateTransaction(transaction: Transaction)
    
    suspend fun deleteTransaction(transaction: Transaction)
    
    suspend fun deleteTransactionById(id: Long)
    
    suspend fun getMonthlyExpenseTotal(year: Int, month: Int): Double
    
    suspend fun getMonthlyIncomeTotal(year: Int, month: Int): Double
}
