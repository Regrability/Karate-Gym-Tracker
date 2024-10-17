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
    isDarkTh: Boolean
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
                modifier = Modifier.fillMaxSize(),
                Alignment.TopCenter
            ) {
                // Фоновое изображение
                Image(
                    painter = painterResource(if (isDarkTh) R.drawable.fon5 else R.drawable.fon1),
                    contentDescription = "Фон Профиля",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.padding(top = 100.dp), // Отступы для колонки
                    verticalArrangement = Arrangement.Top, // Располагаем элементы сверху
                    horizontalAlignment = Alignment.CenterHorizontally // Выравниваем по левому краю
                ) {

                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top // Выравниваем по вертикали
                    ) {
                        // Картинка пользователя
                        Image(
                            painter = painterResource(id = user!!.profilePicture),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(140.dp), // Уменьшенное изображение
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column(
                            horizontalAlignment = Alignment.Start, // Выравниваем текст по левому краю
                            verticalArrangement = Arrangement.Top
                        ) {
                            // Логин пользователя
                            Text(
                                text = user!!.username, // Убедитесь, что у вас есть это поле
                                style = MaterialTheme.typography.headlineLarge, // Большой шрифт
                                color = if (isDarkTh) Color.White else Color.Black
                                )

                            // Описание пользователя
                            Text(
                                text = user!!.description ?: "No description available",
                                textAlign = TextAlign.Start,
                                color = if (isDarkTh) Color.White else Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Рейтинг в виде звезд
                    Row(
                        horizontalArrangement = Arrangement.Start // Располагаем звезды слева
                    ) {
                        repeat(user!!.rating) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow,
                                modifier = Modifier.size(50.dp) // Увеличенные звезды
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка "Назад"
                    Button(
                        onClick = { goToMainScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Назад")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка "Выйти из аккаунта"
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
