package com.example.makeitso.screens.protectedZone

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.makeitso.common.ext.spacer
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.makeitso.LOGIN_SCREEN
import com.example.makeitso.PROTECTED_SCREEN

@Composable
fun ProtectedScreen(openAndPopUp: (String, String) -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null || user.isAnonymous) {
        LaunchedEffect(Unit) {
            openAndPopUp(LOGIN_SCREEN, PROTECTED_SCREEN)
        }
    } else {
        Log.d("User", user.providerId);
        ProtectedContent()
    }
}

@Composable
fun ProtectedContent() {
    // Задаем фоновый цвет
    val backgroundColor = MaterialTheme.colors.background

    // Основной контент
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок
        Text(
            text = "Welcome to the Protected Zone!",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(16.dp)) // Пробел между заголовком и текстом

        // Описание
        Text(
            text = "This area is only for authenticated users.",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp)) // Пробел перед кнопкой
    }
}

@Preview(showBackground = true)
@Composable
fun ProtectedScreenPreview() {
    ProtectedScreen(openAndPopUp = { _, _ -> }) // Предварительный просмотр с заглушкой навигации
}