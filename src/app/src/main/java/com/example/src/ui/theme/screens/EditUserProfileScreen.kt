package com.example.src.ui.theme.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Импортируем нужные компоненты
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.src.R
import com.example.src.ui.theme.dataBases.UserViewModel

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import android.widget.Toast

@Composable
fun EditUserProfileScreen(
    userId: Int,
    goToUserScreen: () -> Unit,
    onMainScreen : () -> Unit, // Возвращаемся на главный экран
    userViewModel: UserViewModel = viewModel(),
    isDarkTh: Boolean,
    context: Context
) {
    // Запрашиваем данные пользователя по ID
    LaunchedEffect(userId) {
        userViewModel.getUserById(userId)
    }

    // Наблюдаем за данными пользователя
    val user by userViewModel.user.observeAsState()

    // Переменные состояния для редактируемых полей
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var profilePictureBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    // Лаунчер для выбора изображения
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            profilePictureUri = uri
            try {
                profilePictureBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } catch (e: Exception) {
                // Обработка ошибок при получении Bitmap
                e.printStackTrace()
            }
        }
    }

    // Когда данные загружены, заполняем поля
    LaunchedEffect(user) {
        user?.let {
            username = it.username
            description = it.description ?: ""
            rating = it.rating
            profilePictureUri = it.profilePictureURL?.let { Uri.parse(it) } // Установка URI, если есть
        }
    }

    // Отображаем UI, когда данные загружены
    if (user != null) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                // Фоновое изображение
                Image(
                    painter = painterResource(if (isDarkTh) R.drawable.fon5 else R.drawable.fon1),
                    contentDescription = "Фон Профиля",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.padding(top = 100.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Картинка пользователя
                        Image(
                            bitmap = profilePictureBitmap?.asImageBitmap() ?: BitmapFactory.decodeResource(context.resources, R.drawable.defould_icon).asImageBitmap(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(140.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            // Поле для ввода имени
                            TextField(
                                value = username,
                                onValueChange = { username = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = if (isDarkTh) Color.White else Color.Black
                                )
                            )

                            // Поле для ввода описания
                            TextField(
                                value = description,
                                onValueChange = { description = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = if (isDarkTh) Color.White else Color.Black
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Рейтинг в виде звезд
                    Row(horizontalArrangement = Arrangement.Start) {
                        repeat(7) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = if (index < rating) Color.Yellow else Color.Gray,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { rating = index + 1 } // Устанавливаем новый рейтинг
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка для загрузки изображения
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Загрузить изображение")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка "Сохранить"
                    Button(onClick = {
                        // Сохранение URI изображения как строки
                        val profilePictureUrl = profilePictureUri?.toString()
                        userViewModel.updateUser(userId, username, description, rating, profilePictureUrl)
                        val message = "информация пользователя изменена"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        onMainScreen()
                    },modifier = Modifier.fillMaxWidth(0.5f)) // Кнопка на всю ширину
                    {
                        Text("Сохранить")
                    }

                   Button(
                        onClick = goToUserScreen, // Переход на экран регистрации
                        modifier = Modifier.fillMaxWidth(0.5f) // Кнопка на всю ширину
                    ) {
                        Text("Назад") // Текст на кнопке
                    }
                }
            }
        }
    } else {
        // Отображаем текст загрузки, пока данные не пришли
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Загрузка данных...")
        }
    }
}