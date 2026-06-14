package com.example.focusbuddyapp.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.focusbuddyapp.presentation.auth.login.LoginScreen
import com.example.focusbuddyapp.presentation.auth.login.LoginViewModel
import com.example.focusbuddyapp.presentation.auth.login.LoginViewModelFactory
import com.example.focusbuddyapp.presentation.auth.register.RegisterScreen
import com.example.focusbuddyapp.presentation.auth.register.RegisterViewModel
import com.example.focusbuddyapp.presentation.auth.register.RegisterViewModelFactory
import com.example.focusbuddyapp.presentation.dashboard.DashboardScreen
import com.example.focusbuddyapp.presentation.dashboard.DashboardViewModel
import com.example.focusbuddyapp.presentation.dashboard.DashboardViewModelFactory
import com.example.focusbuddyapp.presentation.focus.FocusScreen
import com.example.focusbuddyapp.presentation.focus.FocusViewModel
import com.example.focusbuddyapp.presentation.focus.FocusViewModelFactory
import com.example.focusbuddyapp.presentation.profile.ProfileScreen
import com.example.focusbuddyapp.presentation.profile.ProfileViewModel
import com.example.focusbuddyapp.presentation.profile.ProfileViewModelFactory
import com.example.focusbuddyapp.presentation.progress.ProgressScreen
import com.example.focusbuddyapp.presentation.progress.ProgressViewModel
import com.example.focusbuddyapp.presentation.progress.ProgressViewModelFactory
import com.example.focusbuddyapp.presentation.splash.SplashScreen
import com.example.focusbuddyapp.presentation.splash.SplashViewModel
import com.example.focusbuddyapp.presentation.splash.SplashViewModelFactory
import com.example.focusbuddyapp.presentation.task.addedit.AddEditTaskScreen
import com.example.focusbuddyapp.presentation.task.addedit.AddEditTaskViewModel
import com.example.focusbuddyapp.presentation.task.addedit.AddEditTaskViewModelFactory
import com.example.focusbuddyapp.presentation.task.detail.TaskDetailScreen
import com.example.focusbuddyapp.presentation.task.detail.TaskDetailViewModel
import com.example.focusbuddyapp.presentation.task.detail.TaskDetailViewModelFactory
import com.example.focusbuddyapp.presentation.task.list.TaskListScreen
import com.example.focusbuddyapp.presentation.task.list.TaskListViewModel
import com.example.focusbuddyapp.presentation.task.list.TaskListViewModelFactory
import com.example.focusbuddyapp.ui.components.FocusBuddyBottomNav
import com.example.focusbuddyapp.di.AppModule

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    
    val isFocusLocked by AppModule.userPreferences.isFocusLocked.collectAsState(initial = false)

    val showBottomNav = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                FocusBuddyBottomNav(
                    navController = navController,
                    currentRoute = currentRoute,
                    isFocusLocked = isFocusLocked
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ── Auth Flow ──────────────────────────────────────────────────
            composable(Screen.Splash.route) {
                val vm: SplashViewModel = viewModel(factory = SplashViewModelFactory())
                SplashScreen(
                    viewModel = vm,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory())
                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }

            composable(Screen.Register.route) {
                val vm: RegisterViewModel = viewModel(factory = RegisterViewModelFactory())
                RegisterScreen(
                    viewModel = vm,
                    onRegisterSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            // ── Main Bottom Nav Screens ────────────────────────────────────
            composable(Screen.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(factory = DashboardViewModelFactory())
                DashboardScreen(
                    viewModel = vm,
                    onNavigateToAddTask = { navController.navigate(Screen.AddEditTask.createRoute()) },
                    onNavigateToTask = { taskId -> navController.navigate(Screen.TaskDetail.createRoute(taskId)) },
                    onNavigateToTimer = { navController.navigate(Screen.FocusTimer.route) },
                    onNavigateToTaskList = { navController.navigate(Screen.TaskList.route) },
                    onNavigateToProgress = { navController.navigate(Screen.Progress.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.TaskList.route) {
                val vm: TaskListViewModel = viewModel(factory = TaskListViewModelFactory())
                TaskListScreen(
                    viewModel = vm,
                    onNavigateToTask = { taskId -> navController.navigate(Screen.TaskDetail.createRoute(taskId)) },
                    onNavigateToAddTask = { navController.navigate(Screen.AddEditTask.createRoute()) },
                    onNavigateToTimer = { navController.navigate(Screen.FocusTimer.route) }
                )
            }

            composable(Screen.FocusTimer.route) {
                val vm: FocusViewModel = viewModel(factory = FocusViewModelFactory())
                FocusScreen(
                    viewModel = vm,
                    onNavigateToTaskList = { navController.navigate(Screen.TaskList.route) }
                )
            }

            composable(Screen.Progress.route) {
                val vm: ProgressViewModel = viewModel(factory = ProgressViewModelFactory())
                ProgressScreen(
                    viewModel = vm,
                    onNavigateToTaskList = { navController.navigate(Screen.TaskList.route) }
                )
            }

            composable(Screen.Profile.route) {
                val vm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
                ProfileScreen(
                    viewModel = vm,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }

            // ── Task Detail ───────────────────────────────────────────────
            composable(
                route = Screen.TaskDetail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.IntType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
                val vm: TaskDetailViewModel = viewModel(factory = TaskDetailViewModelFactory(taskId))
                TaskDetailScreen(
                    viewModel = vm,
                    onNavigateToEdit = { navController.navigate(Screen.AddEditTask.createRoute(taskId)) },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTimer = { navController.navigate(Screen.FocusTimer.route) }
                )
            }

            // ── Add / Edit Task ───────────────────────────────────────────
            composable(
                route = Screen.AddEditTask.route,
                arguments = listOf(navArgument(Screen.AddEditTask.ARG_TASK_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt(Screen.AddEditTask.ARG_TASK_ID)
                    ?.takeIf { it != -1 }
                val vm: AddEditTaskViewModel = viewModel(factory = AddEditTaskViewModelFactory(taskId))
                AddEditTaskScreen(
                    viewModel = vm,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}
