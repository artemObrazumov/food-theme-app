package com.foodthemeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodthemeapp.components.BottomNavigationBar
import com.foodthemeapp.components.TopAppBar
import com.foodthemeapp.screen.authorization.AuthorizationViewModel
import com.foodthemeapp.ui.theme.FoodThemeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val userdata = FoodApp.googleAuthUiClient.getSignedInUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodThemeAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    topBar = {
                        TopAppBar(
                            avatarUrl = userdata?.profilePictureUrl ?: "",
                            username = userdata?.username ?: "Unknown",
                            onLogoutClicked = {
                                lifecycleScope.launch {
                                    FoodApp.googleAuthUiClient.signOut()
                                    finish()
                                    startActivity(Intent(this@MainActivity, AuthorizationActivity::class.java))
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar()
                    }
                ) { innerPadding ->

                }
            }
        }
    }
}