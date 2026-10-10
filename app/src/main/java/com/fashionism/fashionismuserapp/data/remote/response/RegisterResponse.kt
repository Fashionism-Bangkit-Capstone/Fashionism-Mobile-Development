package com.fashionism.fashionismuserapp.data.remote.response

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class RegisterResponse(val error: String, val message: String)
