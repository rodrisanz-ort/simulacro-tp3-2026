package com.example.tp3_grupo_1

import android.widget.Toast
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.tp3_grupo_1.data.remote.QuotesRepository
import com.example.tp3_grupo_1.model.Quote
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp3_grupo_1.ui.screen.Login
import com.example.tp3_grupo_1.ui.screen.Register
import com.example.tp3_grupo_1.ui.screen.Recover
import com.example.tp3_grupo_1.ui.screen.Frases
import com.example.tp3_grupo_1.ui.screen.Favoritos
import com.example.tp3_grupo_1.ui.navigation.Screen
import com.example.tp3_grupo_1.ui.theme.TP3Grupo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TP3Grupo1Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val quotesRepository = remember { QuotesRepository() }
    var users by remember { mutableStateOf(mapOf<String, String>()) }
    var favorites by remember { mutableStateOf(emptyList<Quote>()) }
    var quotes by remember { mutableStateOf(emptyList<Quote>()) }
    var quotesLoading by remember { mutableStateOf(true) }
    var quotesError by remember { mutableStateOf<String?>(null) }

    suspend fun loadQuotes() {
        quotesLoading = true
        quotesError = null
        quotesRepository.getQuotes()
            .onSuccess { loadedQuotes ->
                quotes = loadedQuotes
                quotesLoading = false
            }
            .onFailure { error ->
                quotesLoading = false
                quotesError = error.message ?: "No se pudieron cargar las frases"
            }
    }

    LaunchedEffect(Unit) {
        loadQuotes()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            Login(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onRecoverClick = { navController.navigate(Screen.Recover.route) },
                onLoginClick = { email, password ->
                    if (users[email] == password) {
                        navController.navigate(Screen.Quote.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Email o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            Register(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { email, password ->
                    if (users.containsKey(email)) {
                        Toast.makeText(
                            context,
                            "Ese email ya está registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        users = users + (email to password)
                        Toast.makeText(
                            context,
                            "Cuenta creada. Iniciá sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                    }
                }
            )
        }
        composable(Screen.Recover.route) {
            Recover(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Quote.route) {
            Frases(
                quotes = quotes,
                isLoading = quotesLoading,
                errorMessage = quotesError,
                favoriteIds = favorites.map { it.id }.toSet(),
                onFavoriteChanged = { quote, isFavorite ->
                    favorites = if (isFavorite) {
                        (favorites + quote).distinctBy { it.id }
                    } else {
                        favorites.filterNot { it.id == quote.id }
                    }
                },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                onRetryClick = { scope.launch { loadQuotes() } }
            )
        }
        composable(Screen.Favorites.route) {
            Favoritos(
                quotes = favorites,
                onDeleteClick = { quote ->
                    favorites = favorites.filterNot { it.id == quote.id }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}