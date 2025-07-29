package com.house.keeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.house.keeper.data.model.Transaction
import com.house.keeper.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TransactionDetailUiState())
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()
    
    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val transaction = transactionRepository.getTransactionById(transactionId)
                _uiState.value = _uiState.value.copy(
                    transaction = transaction,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun deleteTransaction(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val transaction = _uiState.value.transaction
        if (transaction == null) {
            onError("交易不存在")
            return
        }
        
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                onSuccess()
            } catch (e: Exception) {
                onError("删除失败：${e.message ?: "未知错误"}")
            }
        }
    }
}

data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
