package com.ecart.compose.navGraph

sealed class Screen(val route: String) {
    object CART_LIST : Screen(route = "cart_list")
    object ITEM_DETAILS : Screen(route = "cart_details")
    object ADD_CART : Screen(route = "add_cart")
    object PAIR_CODE : Screen(route = "pair_code")
}