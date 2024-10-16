package com.example.src.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.src.ui.theme.screens.LoginScreen
import com.example.src.ui.theme.screens.RegisterScreen
import com.example.src.ui.theme.screens.MainScreen
import com.example.src.ui.theme.screens.GumScreen // Импортируем экран для зала

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    NavHost(
        navController = navController,
        startDestination = "LoginScreen" // Начальный экран – экран входа
    ) {
        // Экран входа
        composable("LoginScreen") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("MainScreen")
                },
                onRegister = {
                    // Переход на экран регистрации
                    navController.navigate("RegisterScreen")
                },
                context = context
            )
        }

        // Экран регистрации
        composable("RegisterScreen") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Переход на экран входа при успешной регистрации
                    navController.navigate("LoginScreen") {
                        // Убираем экран регистрации из стека назад
                        popUpTo("LoginScreen") { inclusive = true }
                    }
                },
                onLogoutScreen = {
                    // Переход обратно на экран входа
                    navController.popBackStack()
                },
                context = context
            )
        }

        // Экран главного меню с темной темой
        composable("MainScreen") {
            SrcTheme(darkTheme = true) {
                MainScreen(
                    changeTheme = {
                        navController.navigate("MainScreenWhiteTheme")
                    },
                    goToGumScreen = { gumId -> // Параметр для перехода на экран зала
                        navController.navigate("GumScreen/$gumId")
                    }
                )
            }
        }

        // Экран главного меню с светлой темой
        composable("MainScreenWhiteTheme") {
            SrcTheme(darkTheme = false) {
                MainScreen(
                    changeTheme = {
                        navController.navigate("MainScreen")
                    },
                    goToGumScreen = { gumId -> // Параметр для перехода на экран зала
                        navController.navigate("GumScreenWhiteTheme/$gumId")
                    }
                )
            }
        }

        // Экран для отображения информации о зале с темной темой
        composable("GumScreen/{gumId}") { backStackEntry ->
            val gumId = backStackEntry.arguments?.getString("gumId")?.toIntOrNull() // Получаем ID зала
            if (gumId != null) {
                SrcTheme(darkTheme = true) {
                    GumScreen(
                        gumId = gumId,
                        onMainScreen = { navController.navigate("MainScreen") } // Передаем ID зала в экран
                    )
                }
            }
        }

        // Экран для отображения информации о зале с белой темой
        composable("GumScreenWhiteTheme/{gumId}") { backStackEntry ->
            val gumId = backStackEntry.arguments?.getString("gumId")?.toIntOrNull() // Получаем ID зала
            if (gumId != null) {
                SrcTheme(darkTheme = false) {
                    GumScreen(
                        gumId = gumId,
                        onMainScreen = { navController.navigate("MainScreenWhiteTheme") } // Передаем ID зала в экран
                    )
                }
            }
        }
    }
}
