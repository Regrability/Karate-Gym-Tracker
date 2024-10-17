package com.example.src.ui.theme

import UserProfileScreen
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.src.ui.theme.screens.LoginScreen
import com.example.src.ui.theme.screens.RegisterScreen
import com.example.src.ui.theme.screens.MainScreen
import com.example.src.ui.theme.screens.GumScreen

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    // Получаем SharedPreferences
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Состояние для входа
    var isLoggedInState by remember {
        mutableStateOf(sharedPreferences.getBoolean("isLoggedIn", false)) // Считываем состояние входа при запуске
    }

    // Состояние для ID пользователя
    var userIdState by remember {
        mutableStateOf(sharedPreferences.getInt("userId", -1)) // Считываем ID пользователя при запуске
    }

    // Состояние для темы
    var isDarkTheme by remember {
        mutableStateOf(sharedPreferences.getBoolean("theme", false)) // false = светлая тема, true = тёмная тема
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedInState) "MainScreen" else "LoginScreen" // Определяем начальный экран
    ) {
        // Экран входа
        composable("LoginScreen") {
            LoginScreen(
                onLoginSuccess = { id ->
                    // Сохраняем состояние входа и ID пользователя
                    sharedPreferences.edit()
                        .putBoolean("isLoggedIn", true)
                        .putInt("userId", id)
                        .apply()

                    // Обновляем состояние для входа и ID пользователя
                    isLoggedInState = true
                    userIdState = id

                    // Переход на главный экран
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

        // Экран главного меню
        composable("MainScreen") {
            SrcTheme(darkTheme = isDarkTheme) { // Применяем тему в зависимости от значения переменной isDarkTheme
                MainScreen(
                    changeTheme = {
                        // Переключаем тему
                        isDarkTheme = !isDarkTheme
                        // Сохраняем новое значение темы в SharedPreferences
                        sharedPreferences.edit().putBoolean("theme", isDarkTheme).apply()
                    },
                    goToGumScreen = { gumId -> // Параметр для перехода на экран зала
                        navController.navigate("GumScreen/$gumId")
                    },
                    goToUserScreen = {
                        navController.navigate("UserProfileScreen")
                    }
                )
            }
        }

        // Экран для отображения информации о зале
        composable("GumScreen/{gumId}") { backStackEntry ->
            val gumId = backStackEntry.arguments?.getString("gumId")?.toIntOrNull() // Получаем ID зала
            if (gumId != null) {
                SrcTheme(darkTheme = isDarkTheme) { // Применяем тему в зависимости от переменной isDarkTheme
                    GumScreen(
                        gumId = gumId,
                        onMainScreen = { navController.navigate("MainScreen") } // Возвращаемся на главный экран
                    )
                }
            }
        }

        // Экран личного кабинета
        composable("UserProfileScreen") {
            UserProfileScreen(
                userId = userIdState,
                goToMainScreen = {
                    navController.navigate("MainScreen")
                },
                goToLoginScreen = {
                    // Обновляем состояние входа и ID пользователя
                    sharedPreferences.edit()
                        .putBoolean("isLoggedIn", false)
                        .putInt("userId", -1)
                        .apply()

                    isLoggedInState = false
                    userIdState = -1

                    // Переход на экран входа
                    navController.navigate("LoginScreen")
                }
            )
        }
    }
}
