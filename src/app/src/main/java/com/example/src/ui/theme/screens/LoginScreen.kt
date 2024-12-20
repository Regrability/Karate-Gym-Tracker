package com.example.src.ui.theme.screens

import com.example.src.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.src.ui.theme.Purple40
import com.example.src.ui.theme.dataBases.UserViewModel
import com.example.src.ui.theme.textColor

@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit, // Колбэк при успешной авторизации
    onRegister: () -> Unit,     // Колбэк для перехода на экран регистрации
    context: Context,           // Контекст для отображения сообщений об ошибках
) {
    // Создание экземпляра UserViewModel
    val userViewModel: UserViewModel = viewModel()

    // Переменные для хранения введённых логина и пароля
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Переменная для хранения ошибок

    // Основной контейнер с наложением элементов
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Изображение используется как фон
        Image(
            painter = painterResource(R.drawable.fon4),
            contentDescription = "Фон",
            contentScale = ContentScale.Crop, // Масштабирование изображения, чтобы покрыть весь экран
            modifier = Modifier.fillMaxSize() // Изображение заполняет весь экран
        )

        // Наложение всех элементов на фон
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp), // Отступ сверху для размещения элементов ниже верхнего края
            verticalArrangement = Arrangement.Top, // Центрирование по вертикали
            horizontalAlignment = Alignment.CenterHorizontally // Центрирование по горизонтали
        ) {

            Spacer(modifier = Modifier.height(16.dp)) // Отступ между элементами

            // Поле для ввода логина
            TextField(
                value = username,
                onValueChange = { username = it }, // Обновление состояния логина
                label = { Text("Логин") }, // Метка для текстового поля
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(0.9f), // Легкая прозрачность для лучшей видимости
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,  // Цвет текста при фокусе
                    unfocusedTextColor = textColor, // Цвет текста без фокуса
                    focusedLabelColor = textColor,  // Цвет метки при фокусе
                    unfocusedLabelColor = textColor // Цвет метки без фокуса
                )
            )

            Spacer(modifier = Modifier.height(8.dp)) // Отступ между полями

            // Поле для ввода пароля
            TextField(
                value = password,
                onValueChange = { password = it }, // Обновление состояния пароля
                label = { Text("Пароль") }, // Метка для текстового поля
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(0.9f), // Легкая прозрачность для лучшей видимости
                visualTransformation = PasswordVisualTransformation(),// Скрытие введённого текста
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,  // Цвет текста при фокусе
                    unfocusedTextColor = textColor, // Цвет текста без фокуса
                    focusedLabelColor = textColor,  // Цвет метки при фокусе
                    unfocusedLabelColor = textColor // Цвет метки без фокуса
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Отступ перед кнопками

            // Кнопка "Войти"
            Button(
                onClick = {
                    // Запрос на получение пользователя с использованием ViewModel
                    userViewModel.getUser(username, password)

                    // Наблюдение за результатом в LiveData
                    userViewModel.user.observeForever { user ->
                        if (user != null) {
                            onLoginSuccess(user.id)
                        } else {
                            errorMessage = "Неверный логин или пароль"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f), // Кнопка на половину ширины экрана
            ) {
                Text("Войти") // Текст на кнопке
            }

            Spacer(modifier = Modifier.height(8.dp)) // Отступ между кнопками

            // Кнопка "Регистрация"
            Button(
                onClick = {
                    // Переход на экран регистрации при нажатии
                    onRegister()
                },
                modifier = Modifier.fillMaxWidth(0.5f) // Кнопка на половину ширины экрана
            ) {
                Text("Регистрация") // Текст на кнопке
            }
            Spacer(modifier = Modifier.height(8.dp)) // Отступ между кнопками

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Предпросмотр экрана с пустыми обработчиками кнопок
    LoginScreen(
        onLoginSuccess = { /* Обработка успешного входа */ },
        onRegister = { /* Обработка регистрации */ },
        context = androidx.compose.ui.platform.LocalContext.current,
    )
}
