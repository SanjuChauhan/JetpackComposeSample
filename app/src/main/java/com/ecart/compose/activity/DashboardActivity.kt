package com.ecart.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.ecart.compose.navGraph.CartNavGraph
import com.ecart.compose.navGraph.Screen
import com.ecart.compose.ui.theme.ScreenBg
import com.ecart.compose.utils.DevicePosture
import com.ecart.compose.utils.ReplyContentType
import com.ecart.compose.utils.isBookPosture
import com.ecart.compose.utils.isSeparating
import com.google.accompanist.adaptive.calculateDisplayFeatures

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class DashboardActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            val displayFeatures = calculateDisplayFeatures(this)

            navController = rememberNavController()
            MainContent(
                modifier = Modifier.fillMaxSize(),
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                navController = navController,
            )
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    navController: NavHostController = rememberNavController(),
) {
    Surface(
        modifier = modifier, color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Column {
                AppToolBar(navController)
            }
            CartNavGraph(
                navController = navController,
                context = LocalContext.current,
                replyContentType = getContentType(windowSize, displayFeatures)
            )
        }
    }
}

private fun getContentType(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
): ReplyContentType {
    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    val contentType = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> ReplyContentType.SINGLE_PANE
        WindowWidthSizeClass.Medium -> if (foldingDevicePosture != DevicePosture.NormalPosture) {
            ReplyContentType.DUAL_PANE
        } else {
            ReplyContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Expanded -> ReplyContentType.DUAL_PANE
        else -> ReplyContentType.SINGLE_PANE
    }

    return contentType
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(navController: NavHostController = rememberNavController()) {
    TopAppBar(title = {
        Text(text = "Shopping")
    }, navigationIcon = {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
        }
    }, actions = {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notification",
            )
        }
        IconButton(onClick = {
            navController.navigate(Screen.ADD_CART.route)
        }) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Shopping Cart",
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = ScreenBg,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White,
        actionIconContentColor = Color.White
    )
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun MainContentPreview() {
    MainContent(
        modifier = Modifier.fillMaxSize(),
        windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
        displayFeatures = emptyList()
    )
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun MainContentPreviewTablet() {
    MainContent(
        modifier = Modifier.fillMaxSize(),
        windowSize = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp)),
        displayFeatures = emptyList(),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun MainContentPreviewTabletPortrait() {
    MainContent(
        modifier = Modifier.fillMaxSize(),
        windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
        displayFeatures = emptyList(),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun MainContentPreviewDesktop() {
    MainContent(
        modifier = Modifier.fillMaxSize(),
        windowSize = WindowSizeClass.calculateFromSize(DpSize(1100.dp, 600.dp)),
        displayFeatures = emptyList(),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun MainContentPreviewDesktopPortrait() {
    MainContent(
        modifier = Modifier.fillMaxSize(),
        windowSize = WindowSizeClass.calculateFromSize(DpSize(600.dp, 1100.dp)),
        displayFeatures = emptyList(),
    )
}

