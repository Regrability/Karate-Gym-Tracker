import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // Импортируем нужные компоненты
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

@Composable
fun UserProfileScreen(
    userId: Int,
    goToMainScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
) {
    // Запрашиваем данные пользователя по ID
    LaunchedEffect(userId) {
        userViewModel.getUserById(userId)
    }

    // Наблюдаем за данными пользователя
    val user by userViewModel.user.observeAsState()

    // Отображаем UI, когда данные загрузились
    if (user != null) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center // Центрируем контент в Box
            ) {
                // Изображение используется как фон
                Image(
                    painter = painterResource(R.drawable.fon1),
                    contentDescription = "Фон Профиля",
                    contentScale = ContentScale.Crop, // Масштабирование изображения, чтобы покрыть весь экран
                    modifier = Modifier.fillMaxSize() // Изображение заполняет весь экран
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Центрируем по горизонтали
                    verticalArrangement = Arrangement.Center // Центрируем по вертикали
                ) {
                    // Картинка пользователя
                    Image(
                        painter = painterResource(id = user!!.profilePicture),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(150.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Рейтинг в виде звезд
                    Row(
                        horizontalArrangement = Arrangement.Center // Центрируем звезды
                    ) {
                        repeat(user!!.rating) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Описание пользователя
                    Text(
                        text = user!!.description ?: "No description available",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Центрируем текст
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { goToMainScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Назад")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { goToLoginScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Выйти из аккаунта")
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
