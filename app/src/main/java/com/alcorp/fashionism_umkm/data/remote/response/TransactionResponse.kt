package com.alcorp.fashionism_umkm.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @field:SerializedName("carts")
    val carts: List<CartsList>? = null,
)

data class CartsList(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("products")
    val products: List<ProductList>? = null,
)

data class ProductList(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("quantity")
    val quantity: String? = null,

    @field:SerializedName("total")
    val total: String? = null,
)