package com.example.bretterverse

import android.app.Application
import com.google.firebase.FirebaseApp

class BretterverseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Initialize FirebaseManager
        FirebaseManager.initialize()
    }

}

