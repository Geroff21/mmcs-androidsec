package com.example.makeitso.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.makeitso.LOGIN_SCREEN
import com.example.makeitso.PROFILE_SCREEN
import com.example.makeitso.PROTECTED_SCREEN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun UserProfileScreen(openAndPopUp: (String, String) -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        // Если пользователь не аутентифицирован, перенаправляем его на экран логина
        openAndPopUp(LOGIN_SCREEN, PROFILE_SCREEN)
    } else {
        UserProfileContent(user)
    }
}

@Composable
fun UserProfileContent(user: FirebaseUser) {
    val coroutineScope = rememberCoroutineScope()
    var isEditing by remember { mutableStateOf(false) } // Управляет режимом редактирования
    var userName by remember { mutableStateOf(user.displayName ?: "") } // Текущее имя пользователя
    var showSaveButton by remember { mutableStateOf(false) } // Показывать кнопку "Сохранить" только если есть изменения

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображение или редактирование имени
        if (isEditing) {
            TextField(
                value = userName,
                onValueChange = { newName ->
                    userName = newName
                    showSaveButton = true // Показывать кнопку "Сохранить" при изменении данных
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(text = "Name: ${user.displayName ?: "No name"}", style = MaterialTheme.typography.h6)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Остальные данные пользователя
        Text(text = "Email: ${user.email ?: "No email"}", style = MaterialTheme.typography.body1)
        Text(
            text = "Authentication method: ${
                if (user.providerData.any { it.providerId == "google.com" }) "Google" else "Email"
            }",
            style = MaterialTheme.typography.body1
        )
        Text(text = "UID: ${user.uid}", style = MaterialTheme.typography.body2)

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки "Редактировать" и "Сохранить"
        if (isEditing) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        updateUserProfile(user, userName)
                        isEditing = false
                        showSaveButton = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = showSaveButton // Кнопка доступна, только если есть изменения
            ) {
                Text("Save")
            }
        } else {
            Button(onClick = { isEditing = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Edit")
            }
        }
    }
}

// Функция для обновления профиля пользователя
suspend fun updateUserProfile(user: FirebaseUser, newName: String) {
    val profileUpdates = userProfileChangeRequest {
        displayName = newName
    }
    user.updateProfile(profileUpdates).await() // Ожидаем завершения обновления
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen(openAndPopUp = { _, _ -> })
}
