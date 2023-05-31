package com.alcorp.fashionism_umkm.ui.home.detail_outfit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.DetailOutfitResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailOutfitViewModel(private val repository: AppRepository) : ViewModel() {
    private val _detailOutfitState = MutableStateFlow(
        UiState(
            Status.LOADING,
            DetailOutfitResponse(),
            ""
        )
    )

    val detailOutfitState = _detailOutfitState

    fun getOutfitDetail(id: String) {
        _detailOutfitState.value = UiState.loading()
        viewModelScope.launch {
            repository.getOutfitDetail(id)
                .catch {
                    _detailOutfitState.value = UiState.error(it.message)
                }

                .collect {
                    _detailOutfitState.value = UiState.success(it.data)
                }
        }
    }
}