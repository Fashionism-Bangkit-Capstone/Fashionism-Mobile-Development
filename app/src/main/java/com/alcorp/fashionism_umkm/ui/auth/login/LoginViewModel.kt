package com.alcorp.fashionism_umkm.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.LoginResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    private val _loginState = MutableStateFlow(
        UiState(
            Status.LOADING,
            LoginResponse(),
            ""
        )
    )

    val loginState = _loginState

    fun loginUser(email: String, password: String) {
        _loginState.value = UiState.loading()
        viewModelScope.launch {
            repository.loginUser(email, password)
                .catch {
                    _loginState.value = UiState.error(it.message.toString())
                }
                .collect {
                    _loginState.value = UiState.success(it.data)
                }
        }
    }
}