package com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcorp.fashionism_umkm.data.AppRepository
import com.alcorp.fashionism_umkm.data.remote.response.ApiResponse
import com.alcorp.fashionism_umkm.data.remote.response.OutfitResponse
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

class AddEditOutfitViewModel(private val repository: AppRepository) : ViewModel() {
    private val _addEditState = MutableStateFlow(
        UiState(
            Status.LOADING,
            ApiResponse(),
            ""
        )
    )

    val addEditState = _addEditState

    fun insertOutfit(token: String, idUser: String, name: RequestBody, description: RequestBody, stock: RequestBody, price: RequestBody, file: MultipartBody.Part) {
        _addEditState.value = UiState.loading()
        val response = repository.insertOutfit(token, idUser, name, description, stock, price, file)
        response.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                _addEditState.value = UiState.success(response.body())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _addEditState.value = UiState.error(t.message)
            }
        })
    }

    fun updateOutfit(token: String, idUser: String, idOutfit: String, name: RequestBody, description: RequestBody, stock: RequestBody, price: RequestBody, file: MultipartBody.Part) {
        _addEditState.value = UiState.loading()
        viewModelScope.launch {
            repository.updateOutfit(token, idUser, idOutfit, name, description, stock, price, file)
                .catch {
                    _addEditState.value = UiState.error(it.message)
                }

                .collect {
                    _addEditState.value = UiState.success(it.data)
                }
        }
    }
}