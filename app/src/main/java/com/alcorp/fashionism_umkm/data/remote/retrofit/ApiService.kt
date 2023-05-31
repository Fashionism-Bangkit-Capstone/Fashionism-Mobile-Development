package com.alcorp.fashionism_umkm.data.remote.retrofit

import com.alcorp.fashionism_umkm.data.remote.response.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun signUpUser (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser (
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("products")
    suspend fun getOutfitList(
//        @Header("Authorization") token: String,
    ): OutfitResponse

    @GET("products/{id}")
    suspend fun getOutfitDetail(
//        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailOutfitResponse

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