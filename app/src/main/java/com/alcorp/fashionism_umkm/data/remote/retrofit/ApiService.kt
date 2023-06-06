package com.alcorp.fashionism_umkm.data.remote.retrofit

import com.alcorp.fashionism_umkm.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/msme/signup")
    suspend fun signUpUser (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("auth/msme/signin")
    suspend fun loginUser (
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("product/{idUser}")
    suspend fun getOutfitList(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
    ): OutfitResponse

    @GET("product/{idUser}/{idOutfit}")
    suspend fun getOutfitDetail(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Path("idOutfit") id: String
    ): DetailOutfitResponse

    @Multipart
    @POST("product/{idUser}")
    fun insertOutfit(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("price") price: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<ApiResponse>

    @DELETE("product/{idUser}/{idOutfit}")
    suspend fun deleteOutfit(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Path("idOutfit") id: String
    ): ApiResponse

    @Multipart
    @PUT("product/{idUser}/{idOutfit}")
    suspend fun updateOutfit(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Path("idOutfit") idOutfit: String,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("stock") stock: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part file: MultipartBody.Part,
    ): ApiResponse

    @GET("profile/msme/{idUser}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
    ): ProfileResponse

    @Multipart
    @PUT("profile/msme/{idUser}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part file: MultipartBody.Part,
    ): ApiResponse

    @FormUrlEncoded
    @PUT("profile/msme/{idUser}/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Path("idUser") idUser: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_password") confirm_password: String
    ): ApiResponse

    @GET("carts")
    suspend fun getTransactionList(
//        @Header("Authorization") token: String,
    ): TransactionResponse

    @GET("carts/{id}")
    suspend fun getTransactionDetail(
//        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CartsList
}