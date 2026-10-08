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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp3_grupo_1.ui.theme.TP3Grupo1Theme

@Composable
fun Login(
    onRegisterClick: () -> Unit = {},
    onRecoverClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> }
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LoginScreen(
        email = email,
        password = password,
        errorMessage = errorMessage,
        onEmailChange = {
            email = it
            errorMessage = null
        },
        onPasswordChange = {
            password = it
            errorMessage = null
        },
        onLoginClick = {
            when {
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                    errorMessage = "Ingresá un email válido"
                password.length < 6 ->
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                else -> onLoginClick(email.trim(), password)
            }
        },
        onRegisterClick = onRegisterClick,
        onRecoverClick = onRecoverClick,
        onGoogleClick = onGoogleClick
    )
}

@Composable
private fun LoginScreen(
    email: String,
    password: String,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Iniciá sesión para guardar tus favoritas",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        TextButton(
            onClick = onRecoverClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("¿Olvidaste tu contraseña?")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Iniciar sesión")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Continuar con Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRegisterClick) {
            Text("¿No tenés una cuenta? Registrate")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    TP3Grupo1Theme {
        Login()
    }
}