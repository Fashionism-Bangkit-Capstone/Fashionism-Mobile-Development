package com.fashionism.fashionismuserapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fashionism.fashionismuserapp.data.db.LoginDataAccount
import com.fashionism.fashionismuserapp.data.db.LoginResponse
import com.fashionism.fashionismuserapp.data.db.RegisterDataAccount

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val message: LiveData<String> = mainRepository.message
    val isLoading: LiveData<Boolean> = mainRepository.isLoading
    val userLogin: LiveData<LoginResponse> = mainRepository.userLogin

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.login(loginDataAccount)
    }

    fun register(registerDataUser: RegisterDataAccount) {
        mainRepository.register(registerDataUser)
    }
}