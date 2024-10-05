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

        // Экран регистрации
        composable("MainScreen") {
            SrcTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }
}
