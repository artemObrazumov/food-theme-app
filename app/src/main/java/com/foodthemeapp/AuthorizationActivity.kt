package com.foodthemeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodthemeapp.auth.GoogleAuthUiClient
import com.foodthemeapp.screen.authorization.AuthorizationScreen
import com.foodthemeapp.screen.authorization.AuthorizationViewModel
import com.foodthemeapp.ui.theme.FoodThemeAppTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class AuthorizationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        if (FoodApp.googleAuthUiClient.getSignedInUser() != null) {
            startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
            overridePendingTransition(0, 0)
        }

        setContent {
            FoodThemeAppTheme {
                val viewModel = viewModel<AuthorizationViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if(result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = FoodApp.googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )

                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                        if(state.isSignInSuccessful) {
                            startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
                        }
                    }

                    AuthorizationScreen(
                        state = state,
                        onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = FoodApp.googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
