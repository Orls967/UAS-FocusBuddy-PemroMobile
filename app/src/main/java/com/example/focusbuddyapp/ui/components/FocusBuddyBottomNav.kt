package com.example.focusbuddyapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.focusbuddyapp.presentation.navigation.BottomNavItem
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun FocusBuddyBottomNav(navController: NavController) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Tasks,
        BottomNavItem.Timer,
        BottomNavItem.Progress,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = SurfaceWhite
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, style = FocusBuddyTypography.labelSmall) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryTerracotta,
                    selectedTextColor = PrimaryTerracotta,
                    unselectedIconColor = SecondaryText,
                    unselectedTextColor = SecondaryText,
                    indicatorColor = SurfaceContainer
                ),
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
