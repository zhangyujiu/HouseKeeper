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
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TransactionListUiState())
    val uiState: StateFlow<TransactionListUiState> = _uiState.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            combine(
                transactionRepository.getAllTransactions(),
                categoryRepository.getAllCategories()
            ) { transactions, categories ->
                val currentState = _uiState.value
                val filteredTransactions = filterTransactions(transactions)
                currentState.copy(
                    transactions = filteredTransactions,
                    categories = categories,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    
    private fun filterTransactions(transactions: List<Transaction>): List<Transaction> {
        val state = _uiState.value
        var filtered = transactions
        
        // 按类型筛选
        if (state.selectedTransactionType != null) {
            filtered = filtered.filter { it.type == state.selectedTransactionType }
        }
        
        // 按分类筛选
        if (state.selectedCategory != null) {
            filtered = filtered.filter { it.category.id == state.selectedCategory.id }
        }
        
        // 按时间范围筛选
        if (state.dateRange != null) {
            filtered = filtered.filter { transaction ->
                transaction.date >= state.dateRange.first && transaction.date <= state.dateRange.second
            }
        }
        
        // 按搜索关键词筛选
        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter { transaction ->
                transaction.description.contains(state.searchQuery, ignoreCase = true) ||
                transaction.category.name.contains(state.searchQuery, ignoreCase = true)
            }
        }
        
        // 按日期排序（最新的在前）
        return filtered.sortedByDescending { it.date }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadTransactions()
    }
    
    fun updateTransactionTypeFilter(type: TransactionType?) {
        _uiState.value = _uiState.value.copy(selectedTransactionType = type)
        loadTransactions()
    }
    
    fun updateCategoryFilter(category: Category?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadTransactions()
    }
    
    fun updateDateRange(startDate: Date?, endDate: Date?) {
        val dateRange = if (startDate != null && endDate != null) {
            Pair(startDate, endDate)
        } else null
        
        _uiState.value = _uiState.value.copy(dateRange = dateRange)
        loadTransactions()
    }
    
    fun setDateRangeToThisMonth() {
        val now = Date()
        val startOfMonth = DateUtils.getStartOfMonth(now)
        val endOfMonth = DateUtils.getEndOfMonth(now)
        updateDateRange(startOfMonth, endOfMonth)
    }
    
    fun setDateRangeToThisWeek() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        
        // 设置到本周一
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeek = calendar.time
        
        // 设置到本周日
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeek = calendar.time
        
        updateDateRange(startOfWeek, endOfWeek)
    }
    
    fun clearAllFilters() {
        _uiState.value = TransactionListUiState(
            transactions = _uiState.value.transactions,
            categories = _uiState.value.categories,
            isLoading = false
        )
        loadTransactions()
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                // 重新加载数据
                loadTransactions()
            } catch (e: Exception) {
                // TODO: 处理错误
            }
        }
    }
}

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedTransactionType: TransactionType? = null,
    val selectedCategory: Category? = null,
    val dateRange: Pair<Date, Date>? = null,
    val isLoading: Boolean = true
)
