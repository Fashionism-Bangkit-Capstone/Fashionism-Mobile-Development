package com.fashionism.fashionismuserapp.data.remote.response

import com.fashionism.fashionismuserapp.data.model.ProfileDetail

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
data class ResponseGetProfile(val error: Boolean, val data: ProfileDetail)
