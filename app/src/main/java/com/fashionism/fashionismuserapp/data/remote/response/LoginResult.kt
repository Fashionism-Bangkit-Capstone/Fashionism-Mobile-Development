package com.fashionism.fashionismuserapp.data.remote.response

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class LoginResult(val id: Int, val name: String, val email: String, val access_token: String)
