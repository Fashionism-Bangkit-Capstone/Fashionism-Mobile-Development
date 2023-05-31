package com.alcorp.fashionism_umkm.ui.transaction.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.CartsList
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: AppRepository) : ViewModel() {
    private val _productState = MutableStateFlow(
        UiState(
            Status.LOADING,
            CartsList(),
            ""
        )
    )

    val productState = _productState

    fun getTransactionDetail(id: String) {
        _productState.value = UiState.loading()
        viewModelScope.launch {
            repository.getTransactionDetail(id)
                .catch {
                    _productState.value = UiState.error(it.message)
                }

                .collect {
                    _productState.value = UiState.success(it.data)
                }
        }
    }
}