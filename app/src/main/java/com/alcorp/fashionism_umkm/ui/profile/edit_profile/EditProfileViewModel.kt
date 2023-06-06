package com.alcorp.fashionism_umkm.ui.profile.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.ApiResponse
import com.alcorp.fashionism_umkm.utils.Status
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val repository: AppRepository) : ViewModel() {
    private val _editProfileState = MutableStateFlow(
        UiState(
            Status.LOADING,
            ApiResponse(),
            ""
        )
    )

    val editProfileState = _editProfileState

    fun updateProfile(token: String, idUser: String, name: RequestBody, email: RequestBody, phone: RequestBody, address: RequestBody, file: MultipartBody.Part) {
        _editProfileState.value = UiState.loading()
        viewModelScope.launch {
            repository.updateProfile(token, idUser, name, email, phone, address, file)
                .catch {
                    _editProfileState.value = UiState.error(it.message)
                }

                .collect {
                    _editProfileState.value = UiState.success(it.data)
                }
        }
    }
}