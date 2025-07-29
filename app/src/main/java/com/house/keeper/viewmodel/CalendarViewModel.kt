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
class CalendarViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    
    init {
        loadCalendarData()
    }
    
    private fun loadCalendarData() {
        viewModelScope.launch {
            combine(
                transactionRepository.getAllTransactions(),
                categoryRepository.getAllCategories()
            ) { transactions, categories ->
                val currentMonth = _uiState.value.currentMonth
                val monthTransactions = getTransactionsForMonth(transactions, currentMonth)
                val dailyData = calculateDailyData(monthTransactions)
                
                CalendarUiState(
                    currentMonth = currentMonth,
                    transactions = monthTransactions,
                    categories = categories,
                    dailyData = dailyData,
                    selectedDate = _uiState.value.selectedDate,
                    selectedDateTransactions = getTransactionsForDate(
                        monthTransactions, 
                        _uiState.value.selectedDate
                    ),
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    
    private fun getTransactionsForMonth(transactions: List<Transaction>, month: Date): List<Transaction> {
        val startOfMonth = DateUtils.getStartOfMonth(month)
        val endOfMonth = DateUtils.getEndOfMonth(month)
        
        return transactions.filter { transaction ->
            transaction.date >= startOfMonth && transaction.date <= endOfMonth
        }
    }
    
    private fun getTransactionsForDate(transactions: List<Transaction>, date: Date?): List<Transaction> {
        if (date == null) return emptyList()
        
        val startOfDay = DateUtils.getStartOfDay(date)
        val endOfDay = DateUtils.getEndOfDay(date)
        
        return transactions.filter { transaction ->
            transaction.date >= startOfDay && transaction.date <= endOfDay
        }.sortedByDescending { it.date }
    }
    
    private fun calculateDailyData(transactions: List<Transaction>): Map<Int, DailyData> {
        val dailyMap = mutableMapOf<Int, DailyData>()
        
        transactions.forEach { transaction ->
            val calendar = Calendar.getInstance()
            calendar.time = transaction.date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            
            val existing = dailyMap[dayOfMonth] ?: DailyData(
                day = dayOfMonth,
                income = 0.0,
                expense = 0.0,
                transactionCount = 0
            )
            
            when (transaction.type) {
                TransactionType.INCOME -> {
                    dailyMap[dayOfMonth] = existing.copy(
                        income = existing.income + transaction.amount,
                        transactionCount = existing.transactionCount + 1
                    )
                }
                TransactionType.EXPENSE -> {
                    dailyMap[dayOfMonth] = existing.copy(
                        expense = existing.expense + transaction.amount,
                        transactionCount = existing.transactionCount + 1
                    )
                }
            }
        }
        
        return dailyMap
    }
    
    fun navigateToMonth(month: Date) {
        _uiState.value = _uiState.value.copy(
            currentMonth = month,
            selectedDate = null
        )
        loadCalendarData()
    }
    
    fun navigateToPreviousMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.currentMonth
        calendar.add(Calendar.MONTH, -1)
        navigateToMonth(calendar.time)
    }
    
    fun navigateToNextMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.currentMonth
        calendar.add(Calendar.MONTH, 1)
        navigateToMonth(calendar.time)
    }
    
    fun navigateToToday() {
        val today = Date()
        navigateToMonth(today)
        selectDate(today)
    }
    
    fun selectDate(date: Date) {
        val selectedDateTransactions = getTransactionsForDate(_uiState.value.transactions, date)
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            selectedDateTransactions = selectedDateTransactions
        )
    }
    
    fun clearSelectedDate() {
        _uiState.value = _uiState.value.copy(
            selectedDate = null,
            selectedDateTransactions = emptyList()
        )
    }
    
    fun getMonthlyTotal(): Pair<Double, Double> {
        val transactions = _uiState.value.transactions
        val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        return Pair(income, expense)
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                // 重新加载数据
                loadCalendarData()
            } catch (e: Exception) {
                // TODO: 处理错误
            }
        }
    }
}

data class CalendarUiState(
    val currentMonth: Date = Date(),
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val dailyData: Map<Int, DailyData> = emptyMap(),
    val selectedDate: Date? = null,
    val selectedDateTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
)

data class DailyData(
    val day: Int,
    val income: Double,
    val expense: Double,
    val transactionCount: Int
) {
    val netIncome: Double get() = income - expense
    val hasTransactions: Boolean get() = transactionCount > 0
}
