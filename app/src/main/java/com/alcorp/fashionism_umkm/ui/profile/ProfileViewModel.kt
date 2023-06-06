package com.alcorp.fashionism_umkm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.OutfitResponse
import com.alcorp.fashionism_umkm.data.remote.response.ProfileResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: AppRepository) : ViewModel() {

    private val _profileState = MutableStateFlow(
        UiState(
            Status.LOADING,
            ProfileResponse(),
            ""
        )
    )

    val profileState = _profileState

    fun getProfile(token: String, idUser: String) {
        _profileState.value = UiState.loading()
        viewModelScope.launch {
            repository.getProfile(token, idUser)
                .catch {
                    _profileState.value = UiState.error(it.message)
                }

                .collect {
                    _profileState.value = UiState.success(it.data)
                }
        }
    }
}