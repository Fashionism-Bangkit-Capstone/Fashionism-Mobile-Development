package com.fashionism.fashionismuserapp.data.remote.response

// Property names mirror the API's JSON keys (Gson, no @SerializedName): rename only together with the API.
/** Shape of every response that only carries a status message, including error bodies. */
data class MessageResponse(val error: Boolean? = null, val message: String? = null)
