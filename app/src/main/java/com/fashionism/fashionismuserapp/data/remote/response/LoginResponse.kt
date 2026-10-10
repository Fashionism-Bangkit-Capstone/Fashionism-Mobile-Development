package com.fashionism.fashionismuserapp.data.remote.response

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class LoginResponse(val error: String, val message: String, val data: LoginResult)
