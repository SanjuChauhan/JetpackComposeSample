@file:OptIn(ExperimentalMaterial3Api::class)

package com.ecart.compose.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ecart.compose.R
import com.ecart.compose.models.CartCompletItemList
import com.ecart.compose.models.CartList
import com.ecart.compose.ui.theme.CardBg
import com.ecart.compose.ui.theme.RapidorCartAppTheme
import com.ecart.compose.ui.theme.ScreenBg
import com.ecart.compose.viewmodels.CartViewModel
import com.example.example.AddToCartListResponse
import com.google.accompanist.systemuicontroller.rememberSystemUiController

var showDialogs = mutableStateOf(false)
var showEditFlag = mutableStateOf(false)
var selectedPdt: CartCompletItemList? = null
val mSortedListWithPdtName = ArrayList<CartCompletItemList>()
var mPriceArray = ArrayList<Double>()
var mGrandTotal = mutableDoubleStateOf(0.0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposeUiApi
fun AddToCartScreen(
    navController: NavHostController,
    mCartListViewModel: CartViewModel,
    mContext: Context
) {
    val mItemDetails = mCartListViewModel.mSharedCartObject.observeAsState()
    val mItemCartList = mCartListViewModel.mCartListModel.observeAsState(emptyList())
    val mCartList = mCartListViewModel.mCartList.observeAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("1", "2", "3", "more")
    var selectedText by remember { mutableStateOf("") }
    var productPrice by remember { mutableDoubleStateOf(0.0) }
    // mContext = LocalContext.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(0xff091432)
    )
    LaunchedEffect(Unit) {
        mCartListViewModel.loadCartListBasedOUserId()
    }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if (showEditFlag.value) {
        RapidorCartAppTheme {
            editNode(selectedPdt, mCartListViewModel, mItemDetails, mContext)
        }
    }

    if (showDialogs.value) {
        RapidorCartAppTheme {
            BasicAlertDialog(onDismissRequest = {
                showDialogs.value = false
            }
            ) {
                Surface(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.lbl_confirm_add),
                            color = White,
                            fontWeight = FontWeight.W500,
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Normal
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(id = R.string.lbl_select_quantity),
                            color = White,
                            fontWeight = FontWeight.W300,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        val text = remember { mutableStateOf("") }

                        val change: (String) -> Unit = {
                            text.value = it
                        }
                        TextField(
                            value = text.value,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            onValueChange = change
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                onClick = {
                                    showDialogs.value = false

                                },
                                colors = ButtonDefaults.buttonColors(),
                                content = { Text(stringResource(id = R.string.lbl_cancel)) }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                onClick = {
                                    if (text.value.isEmpty()) {
                                        ShowToast("Enter valid item count", mContext)
                                    } else if (text.value.toInt() > mItemDetails.value?.rating?.count!!) {
                                        ShowToast("Enter count is not available in cart", mContext)
                                    } else {
                                        mCartListViewModel.addItemToCartApi(
                                            mItemDetails.value,
                                            mContext, text.value
                                        )
                                        showDialogs.value = false
                                    }


                                },
                                colors = ButtonDefaults.buttonColors(),
                                content = { Text(stringResource(id = R.string.lbl_add)) }
                            )

                        }
                    }


                }
            }
        }
    }
    val mProgressDialog = mCartListViewModel.mProgressDialog.observeAsState()
    Column(
        Modifier
            .background(CardBg)
            .fillMaxSize()
    ) {
        if (mProgressDialog.value == "true" || mProgressDialog.value == "null") {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .size(50.dp), strokeWidth = 5.dp, color = Color(0xff091432)
                )
            }
        } else {
            if (mItemDetails.value?.id != null) {
                Column(
                    Modifier
                        .background(White)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp)
                ) {
                    Row {
                        val painter = rememberAsyncImagePainter(mItemDetails.value?.image)
                        Box(
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp)
                                .size(60.dp)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                                    .aspectRatio(1f)
                            )
                        }
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = mItemDetails.value?.title.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.W700,
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Rs " + mItemDetails.value?.price.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.W700,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Column(Modifier.padding(20.dp)) {
                        OutlinedTextField(
                            value = selectedText,
                            onValueChange = { selectedText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size.toSize()
                                },
                            label = { Text("Count", color = Color.Black) },
                            trailingIcon = {
                                Icon(icon, "contentDescription",
                                    Modifier.clickable { expanded = !expanded })
                            }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(White)
                                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                        ) {
                            suggestions.forEach { label ->
                                DropdownMenuItem(text = {
                                    Text(text = label, color = Color.Black)
                                }, onClick = {
                                    if (label == "more") {
                                        showDialogs.value = true
                                        selectedText = label
                                        expanded = false
                                    } else {
                                        selectedText = label
                                        expanded = false
                                        productPrice = label.toInt() * mItemDetails.value?.price!!
                                        mCartListViewModel.addItemToCartApi(
                                            mItemDetails.value,
                                            mContext,
                                            selectedText
                                        )
                                    }

                                })
                            }

                        }
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Item price : Rs. $productPrice", color = Color.Black)
                }

            }

        }
        LoadCartLazyColumn(mItemCartList.value, mCartList.value, mCartListViewModel, mContext)

    }
    DisplayGrandTotalUI()
}

@Composable
fun DisplayGrandTotalUI() {
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(containerColor = ScreenBg),
            content = { Text("Total Payable : Rs " + mGrandTotal.value.toString(), color = White) }
        )
    }

}


@Composable
fun LoadCartLazyColumn(
    list: List<AddToCartListResponse>,
    mComplteItemList: List<CartList>,
    mCartListViewModel: CartViewModel,
    mContext: Context
) {
    mSortedListWithPdtName.clear()
    for (i in list.indices) {
        for (j in 0..<list[i].products.size) {
            for (k in mComplteItemList.indices) {
                if (list[i].products[j].productId == mComplteItemList[k].id) {
                    val mSortedObj = CartCompletItemList(
                        mComplteItemList[k].title!!, list[i].products[j].productId!!,
                        list[i].products[j].quantity!!, mComplteItemList[k].price!!
                    )
                    mSortedListWithPdtName.add(mSortedObj)
                }
            }
        }
    }

    //calculate total amount from list//
    mPriceArray.clear()
    for (i in 0..<mSortedListWithPdtName.size) {
        val mPrice = mSortedListWithPdtName[i].price * mSortedListWithPdtName[i].quantity
        mPriceArray.add(mPrice)
    }
    mGrandTotal.value = mPriceArray.sum()

    LazyColumn(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 70.dp)) {
        items(mSortedListWithPdtName) { cart ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
                    .clickable {

                    },
                colors = CardDefaults.cardColors(
                    containerColor = White,
                ),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = White,
                    ),
                ) {
                    Column {
                        Text(text = cart.name, color = Color.Black, fontWeight = FontWeight.W700)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Quantity : " + cart.quantity.toString(),
                            color = Color.Black,
                            fontWeight = FontWeight.W400
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row {
                            Text(
                                text = "Total Amount : " + cart.quantity + " * " + cart.price,
                                color = Color.Black,
                                fontWeight = FontWeight.W400
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = " = Rs " + cart.quantity * cart.price,
                                color = Color.Black,
                                fontWeight = FontWeight.W400
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Row {

                            Icon(Icons.Rounded.Edit,
                                contentDescription = "",
                                tint = Color.Black,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        showEditFlag.value = true
                                        selectedPdt = cart
                                    })
                            Spacer(modifier = Modifier.size(8.dp))
                            Icon(Icons.Rounded.Delete,
                                contentDescription = "",
                                tint = Color.Black,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        selectedPdt = cart
                                        mCartListViewModel.deleteProduct(cart, mContext)
                                    })
                        }
                    }

                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editNode(
    cart: CartCompletItemList?,
    mCartListViewModel: CartViewModel,
    mItemDetails: State<CartList?>,
    mContext: Context
) {
    if (showEditFlag.value) {
        BasicAlertDialog(onDismissRequest = {
            showEditFlag.value = false
        }
        ) {
            Surface(
                modifier = Modifier
                    .background(Color.Transparent)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.lbl_edit),
                        color = White,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.lbl_select_quantity),
                        color = White,
                        fontWeight = FontWeight.W300,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    var text = remember { mutableStateOf("") }

                    val change: (String) -> Unit = { it ->
                        text.value = it
                    }
                    TextField(
                        value = text.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        onValueChange = change
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Row {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                            onClick = {
                                showEditFlag.value = false

                            },
                            colors = ButtonDefaults.buttonColors(),
                            content = { Text(stringResource(id = R.string.lbl_cancel)) }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                            onClick = {
                                if (text.value.isEmpty()) {
                                    ShowToast("Enter valid item count", mContext)
                                } else if (text.value.toInt() > cart?.quantity!!) {
                                    ShowToast("Enter count is not available in cart", mContext)
                                } else {
                                    mCartListViewModel.updateCartItem(cart, text, mContext)
                                    showEditFlag.value = false

                                }
                            },
                            colors = ButtonDefaults.buttonColors(),
                            content = { Text(stringResource(id = R.string.lbl_add)) }
                        )
                    }
                }
            }
        }
    }
}

fun ShowToast(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}
