package com.example.src.ui.theme.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.src.R
import com.example.src.ui.theme.dataBases.Gum
import com.example.src.ui.theme.dataBases.GumViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(message: Gum,
                goToGumSreen : (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .combinedClickable(
            onClick = {  },
            onLongClick = {
                goToGumSreen(message.id)
            }
        ), // Добавляем обводку
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(message.imageName),
                contentDescription = "Hall picture",
                modifier = Modifier
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth() // Заполняем оставшееся пространство
            ) {
                Text(
                    text = message.hallName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) // Жирный шрифт
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Тренер: ${message.coache}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Время тренировок:\n${message.trainingTimeAndStudents}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun MainScreen(
    gumViewModel: GumViewModel = viewModel(),
    changeTheme: () -> Unit,
    goToGumScreen: (Int) -> Unit,
    goToUserScreen: () -> Unit
) {
    // Вызываем метод для получения всех залов
    gumViewModel.getAllGums()

    // Используем observeAsState для получения списка Gums
    val gums = gumViewModel.gums.observeAsState(listOf()) // Наблюдаем за LiveData

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Добавляем отступы для всей колонки
        ) {
            // Оборачиваем кнопки в Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium) // Добавляем обводку
                    .padding(8.dp),            // Отступы внутри Row после обводки
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Пространство между кнопками
            ) {
                Button(
                    onClick = { goToUserScreen() },
                    modifier = Modifier
                        .weight(1f)          // Каждая кнопка займет половину ширины
                        .padding(8.dp),        // Отступы вокруг кнопки
                    shape = RectangleShape  // Прямоугольная форма кнопки
                ) {
                    Text("Профиль")
                }
                Button(
                    onClick = { changeTheme() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    shape = RectangleShape  // Прямоугольная форма кнопки
                ) {
                    Text("Сменить тему")
                }
            }

            // Пространство между кнопками и списком
            Spacer(modifier = Modifier.height(16.dp))

            // Отображаем список залов
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(gums.value) { gum ->
                    // Отображаем карточки с залами
                    MessageCard(
                        message = gum,
                        goToGumSreen = goToGumScreen
                    )
                }
            }
        }
    }
}

