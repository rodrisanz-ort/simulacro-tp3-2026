package com.example.tp3_grupo_1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp3_grupo_1.ui.theme.TP3Grupo1Theme

@Composable
fun Register(
    onBackClick: () -> Unit = {},
    onRegisterClick: (email: String, password: String) -> Unit = { _, _ -> }
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmation by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Registrate para guardar tus frases favoritas")
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmation,
            onValueChange = { confirmation = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Repetir contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                when {
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                        errorMessage = "Ingresá un email válido"
                    password.length < 6 ->
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    password != confirmation ->
                        errorMessage = "Las contraseñas no coinciden"
                    else -> onRegisterClick(email.trim(), password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }
        TextButton(onClick = onBackClick) {
            Text("Ya tengo una cuenta")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    TP3Grupo1Theme {
        Register()
    }
}