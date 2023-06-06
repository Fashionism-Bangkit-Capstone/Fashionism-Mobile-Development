package com.alcorp.fashionism_umkm.ui.auth.signup

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

class SignUpViewModel(private val repository: AppRepository) : ViewModel() {
    private val _signUpState = MutableStateFlow(
        UiState(
            Status.LOADING,
            ApiResponse(),
            ""
        )
    )

    val signUpState = _signUpState

    fun signUpUser(name: String, email: String, password: String) {
        _signUpState.value = UiState.loading()
        viewModelScope.launch {
            repository.signUpUser(name, email, password)
                .catch {
                    _signUpState.value = UiState.error(it.message.toString())
                }
                .collect {
                    _signUpState.value = UiState.success(it.data)
                }
        }
    }
}