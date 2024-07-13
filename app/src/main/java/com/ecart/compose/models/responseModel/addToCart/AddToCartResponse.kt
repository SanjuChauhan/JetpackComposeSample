package com.ecart.compose.models.responseModel.addToCart

import com.example.example.AddToCartProducts
import com.google.gson.annotations.SerializedName

data class AddToCartResponse(
    @SerializedName("userId") var userId: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("products") var products: ArrayList<AddToCartProducts> = arrayListOf()
)