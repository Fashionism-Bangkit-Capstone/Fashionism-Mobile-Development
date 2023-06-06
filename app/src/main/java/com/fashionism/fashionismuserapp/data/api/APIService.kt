package com.fashionism.fashionismuserapp.data.api

import com.fashionism.fashionismuserapp.data.db.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST("auth/user/signup")
    fun registerUser(@Body requestRegister: RegisterDataAccount): Call<RegisterResponse>

    @POST("auth/user/signin")
    fun loginUser(@Body requestLogin: LoginDataAccount): Call<LoginResponse>

    @GET("profile/user/{id}")
    fun getUser(@Path("id") userId: Int, @Header("Authorization") token: String): Call<ResponseGetProfile>

    @PUT("profile/user/{id}")
    fun updateUser(@Path("id") userId: Int, @Body requestUpdate: ProfileDetail, @Header("Authorization") token: String): Call<ResponseUpdateProfile>

    @PUT("profile/user/{id}/change-password")
    fun changePassword(@Path("id") userId: Int, @Body requestChangePassword: ChangePassword, @Header("Authorization") token: String): Call<ResponseChangePassword>

    @GET("preferences")
    fun getAllPreferences(@Header("Authorization") token: String): Call<ResponseGetAllPreferences>

    @GET("preferences/user-account-preferences/{id}")
    fun getUserPreferences(@Path("id") userId: Int, @Header("Authorization") token: String): Call<ResponseGetUserPreferences>

    @POST("preferences/set-user-account-preference")
    fun setUserPreferences(@Body requestSetPreferences: UserAccountPreferences, @Header("Authorization") token: String): Call<ResponseSetPreferences>

    @POST("preferences/unset-user-account-preference")
    fun unsetUserPreferences(@Body requestUnsetPreferences: UserAccountPreferences, @Header("Authorization") token: String): Call<ResponseSetPreferences>

    @POST("favorites/add")
    fun addFavorite(@Body productFavorite: ItemFavorite, @Header("Authorization") token: String): Call<ResponseFavorite>

    @POST("favorites/remove")
    fun removeFavorite(@Body productFavorite: ItemFavorite, @Header("Authorization") token: String): Call<ResponseFavorite>

    @GET("favorites/{id}")
    fun getFavoritesUser(@Path("id") userId: Int, @Header("Authorization") token: String): Call<ResponseGetFavorites>

}