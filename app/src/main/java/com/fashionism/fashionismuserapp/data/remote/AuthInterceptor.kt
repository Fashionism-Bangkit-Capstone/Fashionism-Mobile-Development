package com.fashionism.fashionismuserapp.data.remote

import com.fashionism.fashionismuserapp.data.local.UserSession
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val session: UserSession) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // OkHttp runs this on its own background thread, so blocking on the DataStore read is fine
        val token = runBlocking { session.token() }
        val request = chain.request().newBuilder()
            .apply { if (token.isNotBlank()) header("Authorization", "Bearer $token") }
            .build()
        return chain.proceed(request)
    }
}
