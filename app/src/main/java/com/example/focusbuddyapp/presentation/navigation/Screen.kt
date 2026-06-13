package com.example.focusbuddyapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Login       : Screen("login")
    object Register    : Screen("register")
    object Dashboard   : Screen("dashboard")
    object TaskList    : Screen("task_list")
    object FocusTimer  : Screen("focus_timer")
    object Progress    : Screen("progress")
    object Profile     : Screen("profile")

    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }

    object AddEditTask : Screen("add_edit_task?taskId={taskId}") {
        fun createRoute(taskId: Int? = null) =
            if (taskId != null) "add_edit_task?taskId=$taskId" else "add_edit_task"
        const val ARG_TASK_ID = "taskId"
    }
}
