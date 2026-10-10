package com.fashionism.fashionismuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
@Parcelize
data class FashionRecommendation(
    val price_output: List<String>,
    val target_link: List<String>,
) : Parcelable
