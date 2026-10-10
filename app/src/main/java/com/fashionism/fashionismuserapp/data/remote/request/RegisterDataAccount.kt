package com.fashionism.fashionismuserapp.data.remote.request

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class RegisterDataAccount(val name: String, val email: String, val password: String)
