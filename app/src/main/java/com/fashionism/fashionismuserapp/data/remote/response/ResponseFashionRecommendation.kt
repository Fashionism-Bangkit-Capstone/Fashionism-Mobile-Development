package com.fashionism.fashionismuserapp.data.remote.response

import com.fashionism.fashionismuserapp.data.model.FashionRecommendation

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class ResponseFashionRecommendation(val data: FashionRecommendation, val error: Boolean)
