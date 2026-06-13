package com.example.focusbuddyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.focusbuddyapp.presentation.navigation.NavGraph
import com.example.focusbuddyapp.ui.theme.FocusBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusBuddyTheme {
                NavGraph()
            }
        }
    }
}