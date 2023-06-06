package com.alcorp.fashionism_umkm.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.OutfitResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AppRepository) : ViewModel() {
    private val _homeState = MutableStateFlow(
        UiState(
            Status.LOADING,
            OutfitResponse(),
            ""
        )
    )

    val homeState = _homeState

    fun getOutfitList(token: String, idUser: String) {
        _homeState.value = UiState.loading()
        viewModelScope.launch {
            repository.getOutfitList(token, idUser)
                .catch {
                    _homeState.value = UiState.error(it.message)
                }

                .collect {
                    _homeState.value = UiState.success(it.data)
                }
        }
    }
}