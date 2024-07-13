package com.ecart.compose.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecart.compose.models.CartCompletItemList
import com.ecart.compose.models.CartList
import com.ecart.compose.repository.CartRepository
import com.example.example.AddToCartListResponse
import com.example.example.AddToCartProducts
import com.example.example.AddToCartRequest
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    val mCartList = MutableLiveData<ArrayList<CartList>>()
    val mItemDetails = MutableLiveData<CartList>()
    val mSharedCartObject = MutableLiveData<CartList>()
    val mCartListModel = MutableLiveData<ArrayList<AddToCartListResponse>>()
    val mToastObserver: MutableLiveData<String>? = null
    var mProgressDialog = MutableLiveData<String>()

    private val repository = CartRepository()
    fun fetchCartListApi() {
        mProgressDialog.value = "true"
        viewModelScope.launch {
            try {
                val response = repository.getCartList()
                if (response.isSuccessful) {
                    mCartList.value = response.body()
                } else {

                }

                mProgressDialog.value = "false"
            } catch (e: Exception) {
                mProgressDialog.value = "false"
                Log.d("Exception", e.message.toString())
            }
        }

    }

    fun getItemDetailsWithRespectTodItemId(id: Int) {
        mProgressDialog.value = "true"
        viewModelScope.launch {
            try {
                val response = repository.getItemDetailsWithRespectTodItemId(id)
                if (response.isSuccessful) {
                    mItemDetails.value = response.body()
                }
                mProgressDialog.value = "false"
            } catch (e: Exception) {
                mProgressDialog.value = "false"
                Log.d("Exception", e.message.toString())
            }
        }
    }

    fun passSelectedObjectAsArguments(cartList: CartList): CartList {
        mSharedCartObject.value = cartList
        return cartList
    }

    fun addItemToCartApi(item: CartList?, context: Context, count: String) {
        mProgressDialog.value = "true"
        val addToCartRequest = AddToCartRequest()
        val addToCartRequestObj = AddToCartProducts()
        val addToCartProducts = ArrayList<AddToCartProducts>()
        addToCartRequest.date = "2020-02-03"
        addToCartRequest.userId = 2
        addToCartRequestObj.productId = item?.id
        addToCartRequestObj.quantity = count.toInt()
        addToCartProducts.add(addToCartRequestObj)
        addToCartRequest.products = addToCartProducts

        viewModelScope.launch {
            try {
                val response = repository.addItemToCart(addToCartRequest)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Item added to cart", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Add to cart failedt", Toast.LENGTH_LONG).show()
                }
                mProgressDialog.value = "false"
            } catch (e: Exception) {
                mProgressDialog.value = "false"
                Log.d("Exception", e.message.toString())
            }
        }
    }

    // loads data with respect to user id 1 . Id is hardcoded//
    fun loadCartListBasedOUserId() {
        mProgressDialog.value = "true"
        viewModelScope.launch {
            try {
                val response = repository.getListOfCartItems(1)
                if (response.isSuccessful) {
                    mCartListModel.value = response.body()
                }
                mProgressDialog.value = "false"

            } catch (e: Exception) {
                mProgressDialog.value = "false"
                Log.d("Exception", e.message.toString())
            }
        }
    }

    //delete each node from cart based on pdt id//
    fun deleteProduct(cart: CartCompletItemList, context: Context) {
        mProgressDialog.value = "true"
        viewModelScope.launch {
            try {
                val response = repository.deleteCartItem(cart.id)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Item deleted from cart", Toast.LENGTH_LONG).show()
                }

                mProgressDialog.value = "false"

            } catch (e: Exception) {
                Toast.makeText(context, "Item deleted sucessfully", Toast.LENGTH_LONG).show()
                mProgressDialog.value = "false"
            }
        }
    }

    //update cart item//
    fun updateCartItem(
        item: CartCompletItemList,
        countOfPdt: MutableState<String>,
        context: Context
    ) {
        mProgressDialog.value = "true"
        val addToCartRequest = AddToCartRequest()
        val addToCartRequestObj = AddToCartProducts()
        val addToCartProducts = ArrayList<AddToCartProducts>()
        addToCartRequest.date = "2020-02-03"
        addToCartRequest.userId = 2
        addToCartRequestObj.productId = item.id
        addToCartRequestObj.quantity = countOfPdt.value.toInt()
        addToCartProducts.add(addToCartRequestObj)
        addToCartRequest.products = addToCartProducts

        viewModelScope.launch {
            try {
                val response = repository.updateCart(2, addToCartRequest)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Item updated sucessfully", Toast.LENGTH_LONG).show()
                }
                mProgressDialog.value = "false"
            } catch (e: Exception) {
                mProgressDialog.value = "false"
            }
        }

    }

}
