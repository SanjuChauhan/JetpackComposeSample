package com.ecart.compose.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecart.compose.DashboardActivity
import com.ecart.compose.R
import com.ecart.compose.ui.theme.ScreenBg

class SplashaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreenContent()
            Thread {
                Thread.sleep(2000)
                runOnUiThread {
                    startActivity(Intent(this@SplashaActivity, DashboardActivity::class.java))
                    finish()
                }
            }.start()
        }
    }
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ScreenBg),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painterResource(R.drawable.ic_cart),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.lbl_splash),
                modifier = Modifier,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W900,
                fontSize = 24.sp,
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenContentPreview() {
    SplashScreenContent()
}