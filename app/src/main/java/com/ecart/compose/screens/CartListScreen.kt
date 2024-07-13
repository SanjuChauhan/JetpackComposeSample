package com.ecart.compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ecart.compose.R
import com.ecart.compose.models.CartList
import com.ecart.compose.navGraph.Screen
import com.ecart.compose.ui.theme.CardBg
import com.ecart.compose.ui.theme.RateingBg
import com.ecart.compose.ui.theme.ScreenBg
import com.ecart.compose.viewmodels.CartViewModel
import java.util.Locale


@Composable
fun CartList(navController: NavController, mCartListViewModel: CartViewModel) {
    val mCartArrayList = ArrayList<CartList>()
    val searchQuery = remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        mCartListViewModel.fetchCartListApi()
    }

    val userList by mCartListViewModel.mCartList.observeAsState(emptyList())
    val mProgressDialog = mCartListViewModel.mProgressDialog.observeAsState()
    val mprogressVisibility = mutableStateOf(false)

    for (element in userList) {
        mCartArrayList.add(element)
    }
    mprogressVisibility.value = mProgressDialog.value == "true" || mProgressDialog.value == "null"
    Column(
        modifier = Modifier
            .background(CardBg)
            .fillMaxSize()
    ) {
        if (mprogressVisibility.value) {
            CircularProgressIndicator(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .size(50.dp), strokeWidth = 5.dp, color = ScreenBg
            )
        }
        SearchBar(
            value = searchQuery.value,
            onValueChange = { newText -> searchQuery.value = newText },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        val filteredList: ArrayList<CartList> = if (searchQuery.value.isEmpty()) {
            mCartArrayList
        } else {
            val searchResultList = ArrayList<CartList>()
            for (group in mCartArrayList) {
                if (group.title!!.lowercase(Locale.getDefault())
                        .contains(searchQuery.value.lowercase(Locale.getDefault()))
                ) {
                    searchResultList.add(group)
                }
            }
            searchResultList
        }
        LazyColumn(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            items(filteredList) { user ->
                LoadCartListCard(user, navController, mCartListViewModel)
            }
        }
    }

}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search", color = Color.Black) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        ),
        modifier = modifier,
    )
}

@Composable
fun LoadCartListCard(
    item: CartList,
    navController: NavController,
    mCartListViewModel: CartViewModel
) {
    val painter = rememberAsyncImagePainter(
        model = item.image,
        placeholder = rememberVectorPainter(image = Icons.Default.ShoppingCart),
        error = rememberVectorPainter(image = Icons.Default.ShoppingCart)
    )
    val rating by remember { mutableFloatStateOf(item.rating?.rate!!.toFloat()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clickable {
                mCartListViewModel.passSelectedObjectAsArguments(item)
                navController.navigate(Screen.ITEM_DETAILS.route)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .aspectRatio(1f),
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = item.title.toString(),
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W700,
                )
                Spacer(modifier = Modifier.size(8.dp))
                StarRatingBar(
                    rating = rating,
                    onRatingChanged = { newRating ->
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = " ₹ " + item.price.toString(),
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W500,
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp = 32.dp,
    starSpacing: Dp = 4.dp,
    starEmpty: ImageVector = Icons.Default.Star,
    starFilled: ImageVector = Icons.Filled.Star,
    starEmptyColor: Color = Color.LightGray,
    starFilledColor: Color = RateingBg,
) {
    Row(modifier = modifier.selectableGroup()) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) starFilled else starEmpty
            val iconTint = if (isSelected) starFilledColor else starEmptyColor
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .selectable(selected = isSelected) {
                        onRatingChanged(i.toFloat())
                    }
                    .width(starSize)
                    .height(starSize)
            )
            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Composable
fun ProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = colorResource(id = R.color.purple_200),
            strokeWidth = Dp(value = 4F)
        )
    }
}