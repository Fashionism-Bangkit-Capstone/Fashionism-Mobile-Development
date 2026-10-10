package com.fashionism.fashionismuserapp.data.remote.request

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class LoginDataAccount(val email: String, val password: String)
