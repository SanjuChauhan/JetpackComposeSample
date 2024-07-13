package com.example.example

import com.google.gson.annotations.SerializedName


data class AddToCartRequest(

    @SerializedName("userId") var userId: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("products") var products: ArrayList<AddToCartProducts> = arrayListOf()

)