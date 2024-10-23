import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Импортируем нужные компоненты
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.src.R
import com.example.src.ui.theme.dataBases.UserViewModel

@Composable
fun UserProfileScreen(
    userId: Int,
    goToMainScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    goToEditUserProfileScreen : () -> Unit,
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
                            painter = if (user?.profilePictureURL.isNullOrEmpty()) {
                                painterResource(id = user!!.profilePicture) // Дефолтная картинка
                            } else {
                                rememberAsyncImagePainter(model = user!!.profilePictureURL) // Картинка по URL
                            },
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(140.dp),
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

                    Box(
                    modifier = Modifier
                        .width(300.dp) // Ширина внешнего прямоугольника
                        .height(30.dp) // Высота внешнего прямоугольника
                        .background(if (isDarkTh) Color.White else Color.Black) // Серый цвет фона
                ) {
                    // Прямоугольник для рейтинга
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // Занимает всю площадь родительского Box
                            .padding(2.dp) // Отступ внутри внешнего прямоугольника (по желанию)
                            .background(
                                color = when (user!!.rating) {
                                    1 -> Color.White
                                    2 -> Color(0xFFFFA500) // Оранжевый
                                    3 -> Color.Blue
                                    4 -> Color.Yellow
                                    5 -> Color.Green
                                    6 -> Color(0xFFA52A2A) // Коричневый
                                    7 -> Color.Black
                                    else -> Color.Gray // Для случаев, когда рейтинг не соответствует
                                }
                            )
                    )
                }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка "Назад"
                    Button(
                        onClick = { goToMainScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Назад")
                    }

                    Button(
                        onClick = { goToEditUserProfileScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Редактировать") // Выравнивание текста по центру)

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка "Выйти из аккаунта"
                    Button(
                        onClick = { goToLoginScreen() },
                        modifier = Modifier.fillMaxWidth(0.5f),
                        colors = ButtonDefaults.buttonColors(
                           Color.Red, // Задаем красный цвет для кнопки
                           Color.White
                        )
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
