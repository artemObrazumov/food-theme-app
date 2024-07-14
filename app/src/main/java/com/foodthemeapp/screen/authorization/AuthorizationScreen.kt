package com.foodthemeapp.screen.authorization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodthemeapp.R
import com.foodthemeapp.auth.SignInState

@Composable
fun AuthorizationScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Добро пожаловать в JFood!",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Войдите или авторизуйтесь с помощью:",
                style = TextStyle(
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.height(18.dp)
            )
            IconButton(
                onClick = onSignInClick,
            ) {
                Icon(
                    painter = painterResource(id = com.google.android.gms.base.R.drawable.googleg_disabled_color_18),
                    contentDescription = null
                )
            }
        }
    }
}