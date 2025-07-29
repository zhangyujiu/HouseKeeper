package com.house.keeper.data.repository.impl

import com.house.keeper.data.database.dao.CategoryDao
import com.house.keeper.data.database.dao.TransactionDao
import com.house.keeper.data.model.Transaction
import com.house.keeper.data.model.toTransaction
import com.house.keeper.data.model.toEntity
import com.house.keeper.data.model.toCategory
import com.house.keeper.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {
    
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().combine(
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transactionEntity ->
                transactionEntity.toTransaction(
                    categories.find { it.id == transactionEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override suspend fun getTransactionById(id: Long): Transaction? {
        val transactionEntity = transactionDao.getTransactionById(id) ?: return null
        val category = categoryDao.getCategoryById(transactionEntity.categoryId)
        return transactionEntity.toTransaction(category?.toCategory())
    }
    
    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions(limit).combine(
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transactionEntity ->
                transactionEntity.toTransaction(
                    categories.find { it.id == transactionEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate).combine(
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transactionEntity ->
                transactionEntity.toTransaction(
                    categories.find { it.id == transactionEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(categoryId).combine(
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transactionEntity ->
                transactionEntity.toTransaction(
                    categories.find { it.id == transactionEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // month is 0-based in Calendar
        val monthDate = calendar.time

        return transactionDao.getTransactionsByMonth(monthDate).combine(
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transactionEntity ->
                transactionEntity.toTransaction(
                    categories.find { it.id == transactionEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }
    
    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }
    
    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }
    
    override suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
    
    override suspend fun getMonthlyExpenseTotal(year: Int, month: Int): Double {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // month is 0-based in Calendar
        val monthDate = calendar.time
        return transactionDao.getMonthlyExpenseTotal(monthDate)
    }

    override suspend fun getMonthlyIncomeTotal(year: Int, month: Int): Double {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // month is 0-based in Calendar
        val monthDate = calendar.time
        return transactionDao.getMonthlyIncomeTotal(monthDate)
    }
}
