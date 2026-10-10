package com.fashionism.fashionismuserapp.data.repository

import com.fashionism.fashionismuserapp.data.Result
import com.fashionism.fashionismuserapp.data.local.UserSession
import com.fashionism.fashionismuserapp.data.remote.request.LoginDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.LoginResponse
import com.fashionism.fashionismuserapp.data.remote.request.RegisterDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.RegisterResponse
import com.fashionism.fashionismuserapp.data.remote.UserApi
import com.fashionism.fashionismuserapp.data.safeApiCall

class AuthRepository(private val api: UserApi, private val session: UserSession) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        val result = safeApiCall { api.login(LoginDataAccount(email, password)) }
        if (result is Result.Success) {
            session.save(result.data.data.id, result.data.data.access_token)
        }
        return result
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> =
        safeApiCall { api.register(RegisterDataAccount(name, email, password)) }

    suspend fun isLoggedIn(): Boolean = session.isLoggedIn()

    suspend fun logout() = session.clear()
}
