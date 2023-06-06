package com.alcorp.fashionism_umkm.ui.profile.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.ApiResponse
import com.alcorp.fashionism_umkm.data.remote.response.LoginResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: AppRepository) : ViewModel() {
    private val _changePasswordState = MutableStateFlow(
        UiState(
            Status.LOADING,
            ApiResponse(),
            ""
        )
    )

    val changePasswordState = _changePasswordState

    fun changePassword(token: String, idUser: String, old_password: String, new_password: String, confirm_password: String) {
        _changePasswordState.value = UiState.loading()
        viewModelScope.launch {
            repository.changePassword(token, idUser, old_password, new_password, confirm_password)
                .catch {
                    _changePasswordState.value = UiState.error(it.message.toString())
                }
                .collect {
                    _changePasswordState.value = UiState.success(it.data)
                }
        }
    }
}