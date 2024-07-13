package com.ecart.compose.models

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("rate") var rate: Double = 0.0,
    @SerializedName("count") var count: Int = 0
)