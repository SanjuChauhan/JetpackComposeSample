package com.ecart.compose.repository

import com.ecart.compose.models.CartList
import com.ecart.compose.models.responseModel.addToCart.AddToCartResponse
import com.ecart.compose.network.RetrofitInstance
import com.example.example.AddToCartListResponse
import com.example.example.AddToCartRequest
import retrofit2.Response

class CartRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getCartList(): Response<ArrayList<CartList>> {
        return apiService.getCartList()
    }

    suspend fun getItemDetailsWithRespectTodItemId(id: Int): Response<CartList> {
        return apiService.getItemDetailsWithRespectTodItemId(id)
    }

    suspend fun addItemToCart(addToCartRequest: AddToCartRequest): Response<AddToCartResponse> {
        return apiService.addItemToCart(addToCartRequest)
    }

    suspend fun getListOfCartItems(id: Int): Response<ArrayList<AddToCartListResponse>> {
        return apiService.getListOfCartItems(id)
    }

    suspend fun deleteCartItem(id: Int): Response<ArrayList<AddToCartResponse>> {
        return apiService.deleteCartItem(id)
    }

    suspend fun updateCart(
        id: Int,
        addToCartRequest: AddToCartRequest
    ): Response<AddToCartResponse> {
        return apiService.updateCart(id, addToCartRequest)
    }


}