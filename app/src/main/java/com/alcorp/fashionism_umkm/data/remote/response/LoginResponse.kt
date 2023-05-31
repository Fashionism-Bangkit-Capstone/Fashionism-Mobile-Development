package com.alcorp.fashionism_umkm.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)