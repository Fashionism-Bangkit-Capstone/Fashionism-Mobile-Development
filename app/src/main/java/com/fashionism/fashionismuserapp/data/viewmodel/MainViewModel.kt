package com.fashionism.fashionismuserapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fashionism.fashionismuserapp.data.db.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val message: LiveData<String> = mainRepository.message
    val isLoading: LiveData<Boolean> = mainRepository.isLoading
    val userLogin: LiveData<LoginResponse> = mainRepository.userLogin
    val userProfile: LiveData<ResponseGetProfile> = mainRepository.userProfile
    val product: LiveData<List<ProductDetail>> = mainRepository.product
    val category: LiveData<List<Category>> = mainRepository.category
    val productListByCategory: LiveData<List<Product>> = mainRepository.productListByCategory
    val productDetail: LiveData<Product> = mainRepository.productDetail

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.login(loginDataAccount)
    }

    fun register(registerDataUser: RegisterDataAccount) {
        mainRepository.register(registerDataUser)
    }

    fun getProfile(id: Int, token: String) {
        mainRepository.getProfileUser(id, token)
    }

    fun updateProfile(
        id: Int,
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody?,
        address: RequestBody?,
        avatar: MultipartBody.Part,
        token: String
    ) {
        mainRepository.updateProfileUser(id, name, email, phone, address, avatar, token)
    }

    fun updatePassword(id: Int, dataPassword: ChangePassword, token: String) {
        mainRepository.changePasswordUser(id, dataPassword, token)
    }

    fun getProducts() {
        mainRepository.getProductLiked()
    }

    fun getCategories(token: String) {
        mainRepository.getAllCategory(token)
    }

    fun getProductByCategory(id: Int, token: String) {
        mainRepository.getProductByCategory(id, token)
    }

    fun getSpecificProduct(id: Int, token: String) {
        mainRepository.getSpecificProduct(id, token)
    }
}