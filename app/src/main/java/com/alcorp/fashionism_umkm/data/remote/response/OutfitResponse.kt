package com.alcorp.fashionism_umkm.data.remote.response

import com.google.gson.annotations.SerializedName

data class OutfitResponse(
//    @field:SerializedName("error")
//    val error: Boolean,
//
//    @field:SerializedName("message")
//    val message: String,

    @field:SerializedName("products")
    val data: List<OutfitData>? = null
)

data class OutfitData(
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