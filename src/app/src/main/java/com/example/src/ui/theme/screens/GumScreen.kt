package com.example.src.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.src.R
import com.example.src.ui.theme.SrcTheme
import com.example.src.ui.theme.dataBases.Gum
import com.example.src.ui.theme.dataBases.GumViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.src.ui.theme.dataBases.User
import com.example.src.ui.theme.dataBases.UserViewModel


@Composable
fun GumScreen(gumId: Int, gumViewModel: GumViewModel = viewModel(),
              onMainScreen: () -> Unit,     // Колбэк для перехода на экран регистрации
) {
    // Запрашиваем зал по ID, когда экран загружается
    LaunchedEffect(gumId) {
        gumViewModel.getGumById(gumId)
    }

    // Наблюдаем за результатом из ViewModel
    val gum = gumViewModel.selectedGum.observeAsState().value

    // Отображаем UI в зависимости от того, загрузились данные или нет
    if (gum != null) {
        // Установка цвета фона
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Изображение зала
                item {
                    Image(
                        painter = painterResource(id = gum.imageName),
                        contentDescription = "Hall Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                // Название зала
                item {
                    Text(
                        text = "Адрес: " + gum.hallName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Изображение тренера
                item {
                    Image(
                        painter = painterResource(id = gum.coachPhotoId),
                        contentDescription = "Coach Photo",
                        modifier = Modifier
                            .size(250.dp) // Увеличьте размер фото тренера
                            .clip(CircleShape)
                    )
                }

                // Информация о тренере
                item {
                    Text(
                        text = gum.coache,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Text(
                        text = gum.coachDescription,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Время тренировок и информация об учениках с обводкой
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Расписание тренировок:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text(
                        text = gum.trainingTimeAndStudents,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium) // Добавляем обводку
                            .padding(8.dp) // Внутренние отступы внутри обводки
                    )
                }

                // Кнопка в конце списка
                item {
                    Spacer(modifier = Modifier.height(20.dp)) // Отступ перед кнопкой

                    Button(
                        onClick = { onMainScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Назад")
                    }
                }
            }
        }
    } else {
        // Показать что-то, если зал еще не загрузился

    }
}


@Preview
@Composable
fun PrintGumScreen() {
    SrcTheme(darkTheme = true) {
        GumScreen(1, onMainScreen = {})}
}
