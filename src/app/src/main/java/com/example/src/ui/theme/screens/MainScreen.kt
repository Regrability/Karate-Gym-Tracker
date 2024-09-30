package com.example.src.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.src.R
import com.example.src.ui.theme.SrcTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.src.ui.theme.UserViewModel
import androidx.navigation.NavController

data class Message(val author: String, val content: String)



@Composable
fun MessageCard(message: Message) {

    Surface(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.karate2),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth() // Заполняем оставшееся пространство
            ) {
                Text(
                    text = message.author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) // Жирный шрифт
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MainScreen() {
    SrcTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                MessageCard(
                    Message(
                        "Kocherov",
                        "Hello! This is a longer message that takes up more space."
                    )
                )
                MessageCard(
                    Message(
                        "Lox kakoi-to",
                        "Hello! This is a longer message that takes up more space."
                    )
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LightPreview() {
    SrcTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DarkPreview() {
        MainScreen()
}
