package com.fashionism.fashionismuserapp.data.api

import com.fashionism.fashionismuserapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    companion object {
        fun getApiServiceV1(): APIService = create(BuildConfig.API_URL_V1)

        fun getApiServiceV2(): APIService = create(BuildConfig.API_URL_V2)

        fun getApiMLService(): APIService = create(BuildConfig.API_URL_ML)

        private fun create(baseUrl: String): APIService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}
