package com.fashionism.fashionismuserapp.data.repository

import com.fashionism.fashionismuserapp.data.Result
import com.fashionism.fashionismuserapp.data.local.UserSession
import com.fashionism.fashionismuserapp.data.model.FashionRecommendation
import com.fashionism.fashionismuserapp.data.model.ItemFavorite
import com.fashionism.fashionismuserapp.data.model.MessageResponse
import com.fashionism.fashionismuserapp.data.model.Product
import com.fashionism.fashionismuserapp.data.remote.ProductApi
import com.fashionism.fashionismuserapp.data.remote.RecommendationApi
import com.fashionism.fashionismuserapp.data.safeApiCall
import com.fashionism.fashionismuserapp.tools.Helper.reduceFileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProductRepository(
    private val api: ProductApi,
    private val recommendationApi: RecommendationApi,
    private val session: UserSession,
) {
    /** [categoryId] null means every category. */
    suspend fun getProducts(categoryId: Int?): Result<List<Product>> = safeApiCall {
        val response = if (categoryId == null) api.getAllProducts() else api.getProductsByCategory(categoryId)
        response.data.orEmpty()
    }

    suspend fun getMostLikedProducts(): Result<List<Product>> =
        safeApiCall { api.getMostLikedProducts().data.orEmpty() }

    suspend fun getFavorites(): Result<List<Product>> =
        safeApiCall { api.getFavorites(session.userId()).data.orEmpty() }

    suspend fun isFavorite(productId: Int): Result<Boolean> =
        safeApiCall { api.isFavorite(session.userId(), productId).data }

    suspend fun setFavorite(productId: Int, favorite: Boolean): Result<MessageResponse> = safeApiCall {
        val item = ItemFavorite(user_account_id = session.userId(), product_id = productId)
        if (favorite) api.addFavorite(item) else api.removeFavorite(item)
    }

    suspend fun getRecommendation(photo: File): Result<FashionRecommendation> {
        val image = withContext(Dispatchers.IO) { reduceFileImage(photo) }
        val part = MultipartBody.Part.createFormData(
            "file", image.name, image.asRequestBody("image/jpeg".toMediaType())
        )
        return safeApiCall { recommendationApi.getRecommendation(part).data }
    }
}
