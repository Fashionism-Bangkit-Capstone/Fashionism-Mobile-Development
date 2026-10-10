package com.fashionism.fashionismuserapp.data.remote

import com.fashionism.fashionismuserapp.data.model.ChangePassword
import com.fashionism.fashionismuserapp.data.model.ItemFavorite
import com.fashionism.fashionismuserapp.data.model.LoginDataAccount
import com.fashionism.fashionismuserapp.data.model.LoginResponse
import com.fashionism.fashionismuserapp.data.model.MessageResponse
import com.fashionism.fashionismuserapp.data.model.ProductListResponse
import com.fashionism.fashionismuserapp.data.model.RegisterDataAccount
import com.fashionism.fashionismuserapp.data.model.RegisterResponse
import com.fashionism.fashionismuserapp.data.model.ResponseFashionRecommendation
import com.fashionism.fashionismuserapp.data.model.ResponseGetProfile
import com.fashionism.fashionismuserapp.data.model.ResponseIsProductFavorite
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

// The Authorization header is added by AuthInterceptor, not per call.

/** BuildConfig.API_URL_V1: account and profile. */
interface UserApi {
    @POST("auth/user/signup")
    suspend fun register(@Body body: RegisterDataAccount): RegisterResponse

    @POST("auth/user/signin")
    suspend fun login(@Body body: LoginDataAccount): LoginResponse

    @GET("profile/user/{id}")
    suspend fun getProfile(@Path("id") userId: Int): ResponseGetProfile

    @Multipart
    @PUT("profile/user/{id}")
    suspend fun updateProfile(
        @Path("id") userId: Int,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part avatar: MultipartBody.Part,
    ): MessageResponse

    @PUT("profile/user/{id}/change-password")
    suspend fun changePassword(@Path("id") userId: Int, @Body body: ChangePassword): MessageResponse
}

/** BuildConfig.API_URL_V2: catalogue and favorites. */
interface ProductApi {
    @GET("product/all")
    suspend fun getAllProducts(): ProductListResponse

    @GET("product/category/{id}")
    suspend fun getProductsByCategory(@Path("id") categoryId: Int): ProductListResponse

    @GET("product/most-product-you-like")
    suspend fun getMostLikedProducts(): ProductListResponse

    @GET("favorites/{id}")
    suspend fun getFavorites(@Path("id") userId: Int): ProductListResponse

    @GET("favorites/{id}/{productId}")
    suspend fun isFavorite(@Path("id") userId: Int, @Path("productId") productId: Int): ResponseIsProductFavorite

    @POST("favorites/add")
    suspend fun addFavorite(@Body body: ItemFavorite): MessageResponse

    @POST("favorites/remove")
    suspend fun removeFavorite(@Body body: ItemFavorite): MessageResponse
}

/** BuildConfig.API_URL_ML: outfit recommendation from a photo. */
interface RecommendationApi {
    @Multipart
    @POST("recommendation")
    suspend fun getRecommendation(@Part file: MultipartBody.Part): ResponseFashionRecommendation
}
