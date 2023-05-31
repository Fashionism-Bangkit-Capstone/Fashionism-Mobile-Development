package com.alcorp.fashionism_umkm.data

import com.alcorp.fashionism_umkm.data.remote.response.*
import com.alcorp.fashionism_umkm.data.remote.retrofit.ApiService
import com.alcorp.fashionism_umkm.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

    fun getOutfitList() : Flow<UiState<OutfitResponse>> {
        return flow {
            val data = apiService.getOutfitList()
            emit(UiState.success(data))
        }.flowOn(Dispatchers.IO)
    }

    fun getOutfitDetail(id: String) : Flow<UiState<DetailOutfitResponse>> {
        return flow {
            val data = apiService.getOutfitDetail(id)
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