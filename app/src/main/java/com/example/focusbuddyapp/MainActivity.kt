package com.example.focusbuddyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.presentation.navigation.NavGraph
import com.example.focusbuddyapp.ui.theme.FocusBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        com.example.focusbuddyapp.ui.util.NotificationHelper.createNotificationChannel(this)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }
        
        setContent {
            val darkTheme by AppModule.userPreferences.isDarkMode.collectAsState(initial = false)
            FocusBuddyTheme(darkTheme = darkTheme) {
                NavGraph()
            }
        }
    }
}