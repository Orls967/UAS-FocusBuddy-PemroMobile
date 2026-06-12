package com.example.focusbuddyapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Login       : Screen("login")
    object Register    : Screen("register")
    object Dashboard   : Screen("dashboard")
    object TaskList    : Screen("task_list")
    object TaskDetail  : Screen("task_detail/{taskId}") {
        fun createRoute(id: Int) = "task_detail/$id"
    }
    object AddEditTask : Screen("add_edit_task?taskId={taskId}") {
        fun createRoute(id: Int? = null) = if (id != null) "add_edit_task?taskId=$id" else "add_edit_task"
    }
    object FocusTimer  : Screen("focus_timer")
    object Progress    : Screen("progress")
    object Profile     : Screen("profile")
}
