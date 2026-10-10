package com.fashionism.fashionismuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Local catalogue entry (tools/FashionData) shown for a recommendation result. */
@Parcelize
data class FashionDataML(
    val name: String,
    val description: String,
    val storeName: String,
) : Parcelable
