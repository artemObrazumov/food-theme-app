package com.foodthemeapp

import android.app.Application
import com.foodthemeapp.auth.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity

class FoodApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FoodApp.instance = this
    }

    companion object {

        private lateinit var instance: FoodApp

        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                context = instance.applicationContext,
                oneTapClient = Identity.getSignInClient(instance.applicationContext)
            )
        }
    }
}