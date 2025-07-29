package com.house.keeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.repository.CategoryRepository
import com.house.keeper.data.repository.TransactionRepository
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.Transaction
import com.house.keeper.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            val currentDate = Date()
            val startOfMonth = DateUtils.getStartOfMonth(currentDate)
            val endOfMonth = DateUtils.getEndOfMonth(currentDate)
            
            combine(
                transactionRepository.getRecentTransactions(10),
                transactionRepository.getTransactionsByDateRange(startOfMonth, endOfMonth),
                categoryRepository.getAllCategories()
            ) { recentTransactions, monthlyTransactions, categories ->
                
                val monthlyIncome = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }
                
                val monthlyExpense = monthlyTransactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }
                
                val balance = monthlyIncome - monthlyExpense
                
                HomeUiState(
                    recentTransactions = recentTransactions,
                    monthlyIncome = monthlyIncome,
                    monthlyExpense = monthlyExpense,
                    balance = balance,
                    categories = categories,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}

data class HomeUiState(
    val recentTransactions: List<Transaction> = emptyList(),
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val balance: Double = 0.0,
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true
)
