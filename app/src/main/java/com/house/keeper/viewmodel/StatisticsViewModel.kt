package com.house.keeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.Transaction
import com.house.keeper.data.repository.CategoryRepository
import com.house.keeper.data.repository.TransactionRepository
import com.house.keeper.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()
    
    init {
        loadStatistics()
    }
    
    private fun loadStatistics() {
        viewModelScope.launch {
            combine(
                transactionRepository.getAllTransactions(),
                categoryRepository.getAllCategories()
            ) { transactions, categories ->
                val currentState = _uiState.value
                val filteredTransactions = getTransactionsInPeriod(transactions)

                currentState.copy(
                    transactions = filteredTransactions,
                    categories = categories,
                    monthlyData = calculateMonthlyData(filteredTransactions),
                    categoryData = calculateCategoryData(filteredTransactions, categories),
                    totalIncome = calculateTotalIncome(filteredTransactions),
                    totalExpense = calculateTotalExpense(filteredTransactions),
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    
    private fun getTransactionsInPeriod(transactions: List<Transaction>): List<Transaction> {
        val state = _uiState.value
        val now = Date()

        // 首先按时间段筛选
        val periodFilteredTransactions = when (state.selectedPeriod) {
            StatisticsPeriod.THIS_MONTH -> {
                val startOfMonth = DateUtils.getStartOfMonth(now)
                val endOfMonth = DateUtils.getEndOfMonth(now)
                transactions.filter { it.date >= startOfMonth && it.date <= endOfMonth }
            }
            StatisticsPeriod.LAST_MONTH -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                val startOfLastMonth = DateUtils.getStartOfMonth(calendar.time)
                val endOfLastMonth = DateUtils.getEndOfMonth(calendar.time)
                transactions.filter { it.date >= startOfLastMonth && it.date <= endOfLastMonth }
            }
            StatisticsPeriod.THIS_YEAR -> {
                val startOfYear = DateUtils.getStartOfYear(now)
                val endOfYear = DateUtils.getEndOfYear(now)
                transactions.filter { it.date >= startOfYear && it.date <= endOfYear }
            }
            StatisticsPeriod.ALL_TIME -> transactions
        }

        // 然后按交易类型筛选
        return if (state.selectedTransactionType != null) {
            periodFilteredTransactions.filter { it.type == state.selectedTransactionType }
        } else {
            periodFilteredTransactions
        }
    }
    
    private fun calculateMonthlyData(transactions: List<Transaction>): List<MonthlyData> {
        val monthlyMap = mutableMapOf<String, MonthlyData>()
        
        transactions.forEach { transaction ->
            val monthKey = DateUtils.formatMonth(transaction.date)
            val existing = monthlyMap[monthKey] ?: MonthlyData(
                month = monthKey,
                income = 0.0,
                expense = 0.0
            )
            
            when (transaction.type) {
                TransactionType.INCOME -> {
                    monthlyMap[monthKey] = existing.copy(income = existing.income + transaction.amount)
                }
                TransactionType.EXPENSE -> {
                    monthlyMap[monthKey] = existing.copy(expense = existing.expense + transaction.amount)
                }
            }
        }
        
        return monthlyMap.values.sortedBy { it.month }.takeLast(12)
    }
    
    private fun calculateCategoryData(transactions: List<Transaction>, categories: List<Category>): List<CategoryData> {
        val categoryMap = mutableMapOf<Long, CategoryData>()
        
        transactions.forEach { transaction ->
            val categoryId = transaction.category.id
            val existing = categoryMap[categoryId] ?: CategoryData(
                category = transaction.category,
                amount = 0.0,
                transactionCount = 0,
                percentage = 0.0
            )
            
            categoryMap[categoryId] = existing.copy(
                amount = existing.amount + transaction.amount,
                transactionCount = existing.transactionCount + 1
            )
        }
        
        val totalAmount = categoryMap.values.sumOf { it.amount }
        
        return categoryMap.values.map { categoryData ->
            categoryData.copy(
                percentage = if (totalAmount > 0) (categoryData.amount / totalAmount) * 100 else 0.0
            )
        }.sortedByDescending { it.amount }
    }
    
    private fun calculateTotalIncome(transactions: List<Transaction>): Double {
        return transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }
    
    private fun calculateTotalExpense(transactions: List<Transaction>): Double {
        return transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }
    
    fun updatePeriod(period: StatisticsPeriod) {
        _uiState.value = _uiState.value.copy(selectedPeriod = period)
        loadStatistics()
    }
    
    fun updateTransactionType(type: TransactionType?) {
        _uiState.value = _uiState.value.copy(selectedTransactionType = type)
        loadStatistics()
    }
}

data class StatisticsUiState(
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val monthlyData: List<MonthlyData> = emptyList(),
    val categoryData: List<CategoryData> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val selectedPeriod: StatisticsPeriod = StatisticsPeriod.THIS_MONTH,
    val selectedTransactionType: TransactionType? = null,
    val isLoading: Boolean = true
)

data class MonthlyData(
    val month: String,
    val income: Double,
    val expense: Double
) {
    val netIncome: Double get() = income - expense
}

data class CategoryData(
    val category: Category,
    val amount: Double,
    val transactionCount: Int,
    val percentage: Double
)

enum class StatisticsPeriod(val displayName: String) {
    THIS_MONTH("本月"),
    LAST_MONTH("上月"),
    THIS_YEAR("今年"),
    ALL_TIME("全部")
}
