package com.ecart.compose.network

import com.ecart.compose.models.CartList
import com.ecart.compose.models.responseModel.addToCart.AddToCartResponse
import com.example.example.AddToCartListResponse
import com.example.example.AddToCartRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {
    @GET("products")
    suspend fun getCartList(): Response<ArrayList<CartList>>

    @GET("products/{id}")
    suspend fun getItemDetailsWithRespectTodItemId(@Path("id") id: Int): Response<CartList>

    @POST("carts")
    suspend fun addItemToCart(@Body addToCartRequest: AddToCartRequest): Response<AddToCartResponse>

    @GET("carts/user/{id}")
    suspend fun getListOfCartItems(@Path("id") id: Int): Response<ArrayList<AddToCartListResponse>>

    @DELETE("carts/{id}")
    suspend fun deleteCartItem(@Path("id") id: Int): Response<ArrayList<AddToCartResponse>>

    @PUT("carts/{id}")
    suspend fun updateCart(
        @Path("id") id: Int,
        @Body addToCartRequest: AddToCartRequest
    ): Response<AddToCartResponse>
}