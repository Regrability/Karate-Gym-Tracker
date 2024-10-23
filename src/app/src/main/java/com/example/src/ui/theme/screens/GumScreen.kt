package com.example.src.ui.theme.screens

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class NativeLib {
    companion object {
        private var isLibraryLoaded = false

        init {
            try {
                System.loadLibrary("native-lib")
                isLibraryLoaded = true
                Log.d("NativeLib", "Library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                isLibraryLoaded = false
                Log.e("NativeLib", "Failed to load library: ${e.message}")
            }
        }
    }

    private external fun parseSchedule(input: String): String


    private fun parseScheduleFallback(input: String): String {
        // Массив для отображения коротких форм дней недели в полные
        val days = mapOf(
            "Пн" to "Понедельник",
            "Вт" to "Вторник",
            "Ср" to "Среда",
            "Чт" to "Четверг",
            "Пт" to "Пятница",
            "Сб" to "Суббота",
            "Вс" to "Воскресенье"
        )

        val result = StringBuilder() // Используем StringBuilder для построения результата

        // Разделяем входную строку на строки по переводу строки
        val lines = input.split("\n")

        // Начинаем результат с "\n* "
        result.append("I'm from Kotlin)\n• ")

        for (line in lines) {
            // Парсим дни недели
            for ((short, full) in days) {
                if (line.contains(short)) {
                    result.append(full).append(", ")
                }
            }

            // Найдем время
            val timeStart = line.indexOf("-")
            if (timeStart != -1) {
                var timeEnd = line.indexOf("(", timeStart)
                if (timeEnd == -1) timeEnd = line.length // если нет скобки, берем конец строки
                result.append(line.substring(timeStart, timeEnd)).append(" ")
            }

            // Парсим возраст
            val ageStart = line.indexOf("(")
            if (ageStart != -1) {
                result.append("; возраст: ")
                val ageEnd = line.indexOf(")", ageStart)
                if (ageEnd != -1) {
                    result.append(line.substring(ageStart + 1, ageEnd)) // Пропускаем открывающую скобку
                }
            }

            result.append(";\n• ") // Добавляем перевод строки для нового блока
        }

        // Убираем последнюю запятую, если она есть
        if (result.isNotEmpty()) {
            result.setLength(result.length - 4) // Удаляем последние запятые и пробелы
        }
        result.append(".")
        return result.toString()
    }


    fun getMessage(message: String): String {
        return if (isLibraryLoaded) {
            parseSchedule(message) // Вызов нативного метода
        } else {
            parseScheduleFallback(message) // Вызов метода на Kotlin
        }
    }
}



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
    // Инициализация NativeLib
    val nativeLib = remember { NativeLib() }

    // Отображаем UI в зависимости от того, загрузились данные или нет
    if (gum != null) {

        val gumInfo = remember { mutableStateOf("") }

        //Запрос сообщения из нативного кода
        LaunchedEffect(Unit) {
            gumInfo.value = nativeLib.getMessage(gum.trainingTimeAndStudents)
        }
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
                        text = gumInfo.value,
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
