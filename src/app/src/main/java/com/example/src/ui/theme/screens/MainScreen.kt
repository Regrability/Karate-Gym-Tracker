package com.example.src.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.src.ui.theme.dataBases.UserViewModel


@Composable
fun MessageCard(message: Gum) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium), // Добавляем обводку
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
fun MainScreen(gumViewModel: GumViewModel = viewModel()) {


    // Вызываем метод для получения всех залов
    gumViewModel.getAllGums()
    // Используем observeAsState для получения списка Gums
    val gums = gumViewModel.gums.observeAsState(listOf()) // Наблюдаем за LiveData

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(gums.value) { gum ->
                // Отображаем карточки с залами
                MessageCard(
                    message = gum
                )
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun DarkPreview() {
    SrcTheme(darkTheme = false) {
        MainScreen()

    }
}



