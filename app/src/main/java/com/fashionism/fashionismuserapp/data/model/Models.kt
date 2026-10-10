package com.fashionism.fashionismuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.

// Auth
data class LoginDataAccount(val email: String, val password: String)

data class LoginResponse(val error: String, val message: String, val data: LoginResult)

data class LoginResult(val id: Int, val name: String, val email: String, val access_token: String)

data class RegisterDataAccount(val name: String, val email: String, val password: String)

data class RegisterResponse(val error: String, val message: String)

// Profile
data class ResponseGetProfile(val error: Boolean, val data: ProfileDetail)

data class ProfileDetail(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val avatar: String? = null,
)

data class ChangePassword(
    val old_password: String,
    val new_password: String,
    val confirm_password: String
)

/** Shape of every response that only carries a status message, including error bodies. */
data class MessageResponse(val error: Boolean? = null, val message: String? = null)

// Products & favorites
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val stock: Int,
    val price: String,
    val product_image: String,
    val category_id: Int,
    val msme_name: String,
) : Parcelable

data class ProductListResponse(val error: Boolean, val data: List<Product>? = null)

data class ItemFavorite(val user_account_id: Int, val product_id: Int)

data class ResponseIsProductFavorite(val error: Boolean, val data: Boolean)

// Recommendation (ML service)
data class ResponseFashionRecommendation(val data: FashionRecommendation, val error: Boolean)

@Parcelize
data class FashionRecommendation(
    val price_output: List<String>,
    val target_link: List<String>,
) : Parcelable

@Parcelize
data class FashionDataML(
    val name: String,
    val description: String,
    val storeName: String,
) : Parcelable
