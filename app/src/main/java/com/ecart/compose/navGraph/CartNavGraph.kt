package com.ecart.compose.navGraph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ecart.compose.screens.AddToCartScreen
import com.ecart.compose.screens.CartList
import com.ecart.compose.screens.ItemDetails
import com.ecart.compose.utils.ReplyContentType
import com.ecart.compose.viewmodels.CartViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CartNavGraph(navController: NavHostController, context: Context, replyContentType: ReplyContentType) {
    val mCartListViewModel: CartViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.CART_LIST.route) {
        composable(route = Screen.CART_LIST.route) {
            CartList(navController, mCartListViewModel)
        }
        composable(route = Screen.ITEM_DETAILS.route) {
            ItemDetails(navController, mCartListViewModel)
        }
        composable(route = Screen.ADD_CART.route) {
            AddToCartScreen(navController, mCartListViewModel, context)
        }
    }

}