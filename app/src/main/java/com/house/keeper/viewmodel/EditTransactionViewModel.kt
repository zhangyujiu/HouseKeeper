package com.house.keeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.Transaction
import com.house.keeper.data.repository.CategoryRepository
import com.house.keeper.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()
    
    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // 加载交易数据
                val transaction = transactionRepository.getTransactionById(transactionId)
                
                if (transaction != null) {
                    _uiState.value = _uiState.value.copy(
                        transaction = transaction,
                        transactionType = transaction.type,
                        amount = transaction.amount.toString(),
                        selectedCategory = transaction.category,
                        description = transaction.description,
                        date = transaction.date,
                        isLoading = false
                    )
                    
                    // 加载分类数据
                    loadCategories()
                } else {
                    _uiState.value = _uiState.value.copy(
                        transaction = null,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    transaction = null,
                    isLoading = false
                )
            }
        }
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCategories = true)
            
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isLoadingCategories = false
                )
            }
        }
    }
    
    fun updateTransactionType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(
            transactionType = type,
            selectedCategory = null // 重置分类选择
        )
    }
    
    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }
    
    fun updateSelectedCategory(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateDate(date: Date) {
        _uiState.value = _uiState.value.copy(date = date)
    }
    
    fun updateTransaction(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value
        val originalTransaction = state.transaction
        
        if (originalTransaction == null) {
            onError("交易记录不存在")
            return
        }
        
        // 验证输入
        if (state.amount.isBlank() || state.amount.toDoubleOrNull() == null || state.amount.toDouble() <= 0) {
            onError("请输入有效的金额")
            return
        }
        
        if (state.selectedCategory == null) {
            onError("请选择分类")
            return
        }
        
        _uiState.value = _uiState.value.copy(isSaving = true)
        
        viewModelScope.launch {
            try {
                val updatedTransaction = originalTransaction.copy(
                    amount = state.amount.toDouble(),
                    type = state.transactionType,
                    category = state.selectedCategory,
                    description = state.description,
                    date = state.date
                )
                
                transactionRepository.updateTransaction(updatedTransaction)
                
                _uiState.value = _uiState.value.copy(
                    transaction = updatedTransaction,
                    isSaving = false
                )
                
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false)
                onError("更新失败：${e.message}")
            }
        }
    }
    
    fun deleteTransaction(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val transaction = _uiState.value.transaction
        
        if (transaction == null) {
            onError("交易记录不存在")
            return
        }
        
        _uiState.value = _uiState.value.copy(isSaving = true)
        
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false)
                onError("删除失败：${e.message}")
            }
        }
    }
    
    fun getFilteredCategories(): List<Category> {
        return _uiState.value.categories.filter { 
            it.type == _uiState.value.transactionType 
        }
    }
}

data class EditTransactionUiState(
    val transaction: Transaction? = null,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val selectedCategory: Category? = null,
    val description: String = "",
    val date: Date = Date(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingCategories: Boolean = false,
    val isSaving: Boolean = false
)
