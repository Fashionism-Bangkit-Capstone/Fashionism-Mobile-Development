package com.alcorp.fashionism_umkm.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailOutfitResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("stock")
    val stock: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null
)