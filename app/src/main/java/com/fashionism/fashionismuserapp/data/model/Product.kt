package com.fashionism.fashionismuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val stock: Int,
    val price: String,
    val product_image: String,
    val category_id: Int,
    val msme_name: String,
) : Parcelable
