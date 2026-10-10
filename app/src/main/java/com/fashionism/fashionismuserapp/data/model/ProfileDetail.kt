package com.fashionism.fashionismuserapp.data.model

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class ProfileDetail(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val avatar: String? = null,
)
