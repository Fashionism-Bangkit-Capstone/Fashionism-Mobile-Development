package com.fashionism.fashionismuserapp.data.remote.request

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class ChangePassword(
    val old_password: String,
    val new_password: String,
    val confirm_password: String
)
