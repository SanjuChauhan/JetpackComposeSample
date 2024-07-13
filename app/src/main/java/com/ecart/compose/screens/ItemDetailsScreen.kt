package com.ecart.compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ecart.compose.R
import com.ecart.compose.navGraph.Screen
import com.ecart.compose.ui.theme.RapidorCartAppTheme
import com.ecart.compose.ui.theme.RateingBg
import com.ecart.compose.ui.theme.ScreenBg
import com.ecart.compose.viewmodels.CartViewModel

var showDialog = mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetails(navController: NavHostController, mCartListViewModel: CartViewModel) {
    val mItemDetails = mCartListViewModel.mSharedCartObject.observeAsState()

    LaunchedEffect(Unit) {
        mItemDetails.value?.id?.let {
            mCartListViewModel.getItemDetailsWithRespectTodItemId(it)
        }
    }
    var progressDialog by remember { mutableStateOf(false) }

    if (progressDialog) {
        Dialog(
            onDismissRequest = { progressDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }

    if (showDialog.value) {
        RapidorCartAppTheme {
            BasicAlertDialog(onDismissRequest = {
                showDialog.value = false
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
                            text = stringResource(id = R.string.lbl_sure_to_confirm_add),
                            color = White,
                            fontWeight = FontWeight.W300,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                onClick = {
                                    showDialog.value = false

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = White
                                ),
                                content = {
                                    Text(
                                        stringResource(id = R.string.lbl_cancel),
                                        color = Color(0xff091432)
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                onClick = {
                                    mCartListViewModel.passSelectedObjectAsArguments(mItemDetails.value!!)
                                    navController.navigate(Screen.ADD_CART.route)
                                    showDialog.value = false

                                },
                                colors = ButtonDefaults.buttonColors(containerColor = White),
                                content = {
                                    Text(
                                        stringResource(id = R.string.lbl_add),
                                        color = Color(0xff091432)
                                    )
                                }
                            )

                        }
                    }
                }
            }
        }
    }

    mItemDetails.value.let {

        val rating by remember { mutableFloatStateOf(mItemDetails.value?.rating?.rate!!.toFloat()) }

        val painter = rememberAsyncImagePainter(mItemDetails.value?.image)
        Column(
            Modifier
                .background(White)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 80.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
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
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = mItemDetails.value?.title.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Blue,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W700,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Product Details",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .drawBehind {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height - 2.sp.toPx()
                        drawLine(
                            color = Color.Black,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    },
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W900,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = mItemDetails.value?.description.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W400,
            )
            Spacer(modifier = Modifier.size(8.dp))
            StarRatingBarDetailsScreen(
                rating = rating,
                onRatingChanged = {}
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = " Item left : " + mItemDetails.value?.rating?.count.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W700,
            )
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    showDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = ScreenBg),
                content = { Text(stringResource(id = R.string.lbl_add_cart), color = White) }
            )
        }
    }
}

@Composable
fun StarRatingBarDetailsScreen(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    starEmpty: ImageVector = Icons.Default.Star,
    starFilled: ImageVector = Icons.Filled.Star,
    starEmptyColor: Color = Color.LightGray,
    starFilledColor: Color = RateingBg,
) {
    val starSize = 32.dp
    val starSpacing = 4.dp
    Row(
        modifier = Modifier
            .padding(start = 12.dp)
            .selectableGroup()
    ) {
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
