package com.example.focusbuddyapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(Screen.Dashboard.route, "Home", Icons.Filled.Dashboard)
    object Tasks     : BottomNavItem(Screen.TaskList.route, "Tasks", Icons.Filled.Assignment)
    object Timer     : BottomNavItem(Screen.FocusTimer.route, "Timer", Icons.Filled.Timer)
    object Progress  : BottomNavItem(Screen.Progress.route, "Stats", Icons.Filled.BarChart)
    object Profile   : BottomNavItem(Screen.Profile.route, "Profile", Icons.Filled.Person)
}
