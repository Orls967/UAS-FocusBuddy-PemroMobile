package com.example.focusbuddyapp

import android.app.Application
import com.example.focusbuddyapp.di.AppModule

class FocusBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}
