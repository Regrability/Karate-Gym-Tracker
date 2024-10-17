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
import com.example.src.ui.theme.dataBases.User
import com.example.src.ui.theme.dataBases.UserViewModel
import com.example.src.ui.theme.textColor
import kotlin.random.Random



@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit, // Колбэк при успешной авторизации
    onLogoutScreen: () -> Unit,     // Колбэк для перехода на экран регистрации
    context: Context            // Контекст для отображения сообщений об ошибках
) {
    // Создание экземпляра UserViewModel
    val userViewModel: UserViewModel = viewModel()

    // Переменные для хранения введённых логина и пароля
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Переменная для хранения ошибок

    // Основной контейнер с наложением элементов
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Изображение используется как фон
        Image(
            painter = painterResource(R.drawable.fon4), // Замените на ваше изображение
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
                visualTransformation = PasswordVisualTransformation(), // Скрытие введённого текста
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,  // Цвет текста при фокусе
                    unfocusedTextColor = textColor, // Цвет текста без фокуса
                    focusedLabelColor = textColor,  // Цвет метки при фокусе
                    unfocusedLabelColor = textColor // Цвет метки без фокуса
                )
            )

            // Поле для ввода пароля
            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it }, // Обновление состояния пароля
                label = { Text("Повторите пароль") }, // Метка для текстового поля
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(0.9f), // Легкая прозрачность для лучшей видимости
                visualTransformation = PasswordVisualTransformation(), // Скрытие введённого текста
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,  // Цвет текста при фокусе
                    unfocusedTextColor = textColor, // Цвет текста без фокуса
                    focusedLabelColor = textColor,  // Цвет метки при фокусе
                    unfocusedLabelColor = textColor // Цвет метки без фокуса
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Отступ перед кнопками

            Button(
                onClick = {
                    // Запрос на проверку наличия пользователя с таким логином
                    userViewModel.getUserByUsername(username)

                    // Наблюдение за результатом в LiveData
                    userViewModel.user.observeForever { user ->
                        if (user != null) {
                            errorMessage = "Этот логин уже занят"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        else if (username.length < 4 ) {
                            errorMessage = "Логин cлишком короткий"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        else if (password.length < 5) {
                            errorMessage = "Пароль cлишком короткий"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        else if (password != repeatPassword){
                            errorMessage = "непрвильно введён пароль"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        else {
                            // Если пользователя с таким логином нет, добавляем его в базу данных
                            userViewModel.insertUser(User(username = username, password = password, profilePicture = R.drawable.profile_standart, description = "I\'m like karate", rating = Random.nextInt(1, 6)))
                            Toast.makeText(context, "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show()

                            // После успешной регистрации выполняем колбэк
                            onRegisterSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Зарегистрироваться")
            }


            Spacer(modifier = Modifier.height(8.dp)) // Отступ между кнопками

            // Кнопка "Назад"
            Button(
                onClick = onLogoutScreen, // Переход на экран регистрации
                modifier = Modifier.fillMaxWidth(0.5f) // Кнопка на всю ширину
            ) {
                Text("Назад") // Текст на кнопке
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // Предпросмотр экрана с пустыми обработчиками кнопок
    RegisterScreen(
        onRegisterSuccess = { /* TODO: Обработка успешного входа */ },
        onLogoutScreen = { /* TODO: Обработка регистрации */ },
        context = androidx.compose.ui.platform.LocalContext.current
    )
}
