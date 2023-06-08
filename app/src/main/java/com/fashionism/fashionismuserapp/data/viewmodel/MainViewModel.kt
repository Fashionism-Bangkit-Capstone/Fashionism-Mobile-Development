package com.fashionism.fashionismuserapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fashionism.fashionismuserapp.data.db.*

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val message: LiveData<String> = mainRepository.message
    val isLoading: LiveData<Boolean> = mainRepository.isLoading
    val userLogin: LiveData<LoginResponse> = mainRepository.userLogin
    val userProfile: LiveData<ResponseGetProfile> = mainRepository.userProfile
    val product: LiveData<List<ProductDetail>> = mainRepository.product

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.login(loginDataAccount)
    }

    fun register(registerDataUser: RegisterDataAccount) {
        mainRepository.register(registerDataUser)
    }

    fun getProfile(id: Int, token: String) {
        mainRepository.getProfileUser(id, token)
    }

    fun updateProfile(id: Int, dataUser: ProfileDetail, token: String) {
        mainRepository.updateProfileUser(id, dataUser, token)
    }

    fun updatePassword(id: Int, dataPassword: ChangePassword, token: String) {
        mainRepository.changePasswordUser(id, dataPassword, token)
    }

    fun getProducts() {
        mainRepository.getProductLiked()
    }
}