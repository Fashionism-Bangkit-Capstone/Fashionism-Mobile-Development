package com.alcorp.fashionism_umkm.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.TransactionResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: AppRepository) : ViewModel() {
    private val _transactionState = MutableStateFlow(
        UiState(
            Status.LOADING,
            TransactionResponse(),
            ""
        )
    )

    val transactionState = _transactionState

    fun getTransactionList() {
        _transactionState.value = UiState.loading()
        viewModelScope.launch {
            repository.getTransactionList()
                .catch {
                    _transactionState.value = UiState.error(it.message)
                }

                .collect {
                    _transactionState.value = UiState.success(it.data)
                }
        }
    }
}