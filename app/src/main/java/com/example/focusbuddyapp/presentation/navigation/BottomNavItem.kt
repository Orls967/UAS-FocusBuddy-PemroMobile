package com.example.focusbuddyapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Filled.Dashboard,   Screen.Dashboard.route),
    BottomNavItem("Tasks",     Icons.Filled.Assignment,  Screen.TaskList.route),
    BottomNavItem("Timer",     Icons.Filled.Timer,       Screen.FocusTimer.route),
    BottomNavItem("Progress",  Icons.Filled.BarChart,    Screen.Progress.route),
    BottomNavItem("Profile",   Icons.Filled.Person,      Screen.Profile.route),
)

val bottomNavRoutes = bottomNavItems.map { it.route }.toSet()
