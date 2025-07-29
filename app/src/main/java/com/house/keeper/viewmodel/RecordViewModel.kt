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
class RecordViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isLoading = false
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
    
    fun saveTransaction(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value

        // 验证输入
        if (state.amount.isBlank()) {
            onError("请输入金额")
            return
        }

        val amountValue = state.amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
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
                val transaction = Transaction(
                    amount = amountValue,
                    type = state.transactionType,
                    category = state.selectedCategory,
                    description = state.description,
                    date = state.date
                )

                transactionRepository.insertTransaction(transaction)

                // 重置表单
                _uiState.value = RecordUiState(
                    categories = state.categories,
                    isLoading = false
                )

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false)
                onError("保存失败：${e.message ?: "未知错误"}")
            }
        }
    }
    
    fun getFilteredCategories(): List<Category> {
        return _uiState.value.categories.filter { 
            it.type == _uiState.value.transactionType 
        }
    }
}

data class RecordUiState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val selectedCategory: Category? = null,
    val description: String = "",
    val date: Date = Date(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false
)
