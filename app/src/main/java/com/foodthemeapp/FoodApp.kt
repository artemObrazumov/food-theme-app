package com.foodthemeapp

import android.app.Application
import com.foodthemeapp.auth.GoogleAuthUiClient
import com.foodthemeapp.data.Api
import com.google.android.gms.auth.api.identity.Identity

class FoodApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private lateinit var instance: FoodApp

        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                context = instance.applicationContext,
                oneTapClient = Identity.getSignInClient(instance.applicationContext)
            )
        }
        val api by lazy {
            Api()
        }
    }
}