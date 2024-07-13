package com.example.example

import com.google.gson.annotations.SerializedName


data class AddToCartListResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("userId") var userId: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("products") var products: ArrayList<Products> = arrayListOf(),
    @SerializedName("__v") var _v: Int? = null

)