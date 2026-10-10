package com.fashionism.fashionismuserapp.data.remote

import com.fashionism.fashionismuserapp.data.remote.response.ResponseFashionRecommendation
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/** BuildConfig.API_URL_ML: outfit recommendation from a photo. Needs no login. */
interface RecommendationApi {
    @Multipart
    @POST("recommendation")
    suspend fun getRecommendation(@Part file: MultipartBody.Part): ResponseFashionRecommendation
}
