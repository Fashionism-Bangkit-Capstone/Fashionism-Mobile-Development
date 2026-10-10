package com.fashionism.fashionismuserapp.data.remote

import com.fashionism.fashionismuserapp.data.remote.request.ChangePassword
import com.fashionism.fashionismuserapp.data.remote.request.LoginDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.LoginResponse
import com.fashionism.fashionismuserapp.data.remote.response.MessageResponse
import com.fashionism.fashionismuserapp.data.remote.request.RegisterDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.RegisterResponse
import com.fashionism.fashionismuserapp.data.remote.response.ResponseGetProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

/** BuildConfig.API_URL_V1: account and profile. AuthInterceptor adds the Authorization header. */
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
