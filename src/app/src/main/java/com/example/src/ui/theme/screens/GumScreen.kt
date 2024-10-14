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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
fun GumScreen(gumId: Int, gumViewModel: GumViewModel = viewModel()) {

    // Запрашиваем зал по ID, когда экран загружается
    LaunchedEffect(gumId) {
        gumViewModel.getGumById(gumId)
    }

    // Наблюдаем за результатом из ViewModel
    val gum = gumViewModel.selectedGum.observeAsState().value

    // Отображаем UI в зависимости от того, загрузились данные или нет
    if (gum != null) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(gum.imageName),
                    contentDescription = "Hall picture",
                    modifier = Modifier
                        .size(120.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth() // Заполняем оставшееся пространство
                ) {
                    Text(
                        text = gum.hallName,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) // Жирный шрифт
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Тренер: ${gum.coache}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Время тренировок:\n${gum.trainingTimeAndStudents}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    } else {
        // Показать что-то, если зал еще не загрузился
        Text("Загрузка данных...")
    }
}
