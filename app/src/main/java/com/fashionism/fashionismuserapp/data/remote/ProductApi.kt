package com.fashionism.fashionismuserapp.data.remote

import com.fashionism.fashionismuserapp.data.remote.request.ItemFavorite
import com.fashionism.fashionismuserapp.data.remote.response.MessageResponse
import com.fashionism.fashionismuserapp.data.remote.response.ProductListResponse
import com.fashionism.fashionismuserapp.data.remote.response.ResponseIsProductFavorite
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** BuildConfig.API_URL_V2: catalogue and favorites. AuthInterceptor adds the Authorization header. */
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
