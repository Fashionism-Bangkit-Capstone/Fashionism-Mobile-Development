package com.alcorp.fashionism_umkm.data

import com.alcorp.fashionism_umkm.data.remote.response.*
import com.alcorp.fashionism_umkm.data.remote.retrofit.ApiService
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val apiService: ApiService) {
    suspend fun signUpUser(name: String, email: String, password: String) : Flow<UiState<ApiResponse>> {
        return flow {
            val data = apiService.signUpUser(name, email, password)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun loginUser(email: String, password: String) : Flow<UiState<LoginResponse>> {
        return flow {
            val data = apiService.loginUser(email, password)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getOutfitList(token: String, idUser: String) : Flow<UiState<OutfitResponse>> {
        return flow {
            val data = apiService.getOutfitList(token, idUser)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getOutfitDetail(token: String, idUser: String, idOutfit: String) : Flow<UiState<DetailOutfitResponse>> {
        return flow {
            val data = apiService.getOutfitDetail(token, idUser, idOutfit)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun insertOutfit(token: String, idUser: String, name: RequestBody, description: RequestBody, stock: RequestBody, price: RequestBody, file: MultipartBody.Part) =
        apiService.insertOutfit(token, idUser, name, description, stock, price, file)

    fun deleteOutfit(token: String, idUser: String, idOutfit: String) : Flow<UiState<ApiResponse>> {
        return flow {
            val data = apiService.deleteOutfit(token, idUser, idOutfit)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun updateOutfit(token: String, idUser: String, idOutfit: String, name: RequestBody, description: RequestBody, stock: RequestBody, price: RequestBody, file: MultipartBody.Part) : Flow<UiState<ApiResponse>> {
        return flow {
            val data = apiService.updateOutfit(token, idUser, idOutfit, name, description, stock, price, file)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getProfile(token: String, idUser: String) : Flow<UiState<ProfileResponse>> {
        return flow {
            val data = apiService.getProfile(token, idUser)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun updateProfile(token: String, idUser: String, name: RequestBody, email: RequestBody, phone: RequestBody, address: RequestBody, file: MultipartBody.Part) : Flow<UiState<ApiResponse>> {
        return flow {
            val data = apiService.updateProfile(token, idUser, name, email, phone, address, file)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun changePassword(token: String, idUser: String, old_password: String, new_password: String, confirm_password: String) : Flow<UiState<ApiResponse>> {
        return flow {
            val data = apiService.changePassword(token, idUser, old_password, new_password, confirm_password)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getTransactionList() : Flow<UiState<TransactionResponse>> {
        return flow {
            val data = apiService.getTransactionList()
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getTransactionDetail(id: String) : Flow<UiState<CartsList>> {
        return flow {
            val data = apiService.getTransactionDetail(id)
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService)
            }.also { instance = it }
    }
}