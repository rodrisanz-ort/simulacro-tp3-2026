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
import java.security.MessageDigest
import com.example.tp3_grupo_1.data.local.AppDataStore
import com.example.tp3_grupo_1.data.local.AppDatabase
import com.example.tp3_grupo_1.data.local.FavoritesRepository
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
    val appDataStore = remember { AppDataStore(context.applicationContext) }
    val database = remember {
        androidx.room.Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).fallbackToDestructiveMigration().build()
    }
    val favoritesRepository = remember { FavoritesRepository(database.favoriteQuoteDao()) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    var sessionLoaded by remember { mutableStateOf(false) }
    var favorites by remember { mutableStateOf(emptyList<Quote>()) }
    var currentQuote by remember { mutableStateOf<Quote?>(null) }
    var quotesLoading by remember { mutableStateOf(true) }
    var quotesError by remember { mutableStateOf<String?>(null) }

    suspend fun loadQuote() {
        quotesLoading = true
        quotesError = null
        quotesRepository.getRandomQuote()
            .onSuccess { loadedQuote ->
                currentQuote = loadedQuote
                quotesLoading = false
            }
            .onFailure { error ->
                quotesLoading = false
                quotesError = error.message ?: "No se pudieron cargar las frases"
            }
    }

    LaunchedEffect(Unit) {
        currentUser = appDataStore.getSession()
        if (currentUser != null) {
            favorites = favoritesRepository.getFavorites(currentUser!!)
        }
        sessionLoaded = true
        quotesLoading = false
    }

    LaunchedEffect(currentUser, sessionLoaded) {
        if (sessionLoaded && currentUser != null) {
            loadQuote()
        }
    }

    if (!sessionLoaded) {
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Screen.Login.route else Screen.Quote.route
    ) {
        composable(Screen.Login.route) {
            Login(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onRecoverClick = { navController.navigate(Screen.Recover.route) },
                onLoginClick = { email, password ->
                    scope.launch {
                        if (appDataStore.isValidUser(email, password.sha256())) {
                            currentUser = email
                            favorites = favoritesRepository.getFavorites(email)
                            appDataStore.saveSession(email)
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
                }
            )
        }
        composable(Screen.Register.route) {
            Register(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { email, password ->
                    scope.launch {
                        if (appDataStore.register(email, password.sha256())) {
                            Toast.makeText(
                                context,
                                "Cuenta creada. Iniciá sesión",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Ese email ya está registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
        composable(Screen.Recover.route) {
            Recover(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Quote.route) {
            Frases(
                quote = currentQuote,
                isLoading = quotesLoading,
                errorMessage = quotesError,
                favoriteIds = favorites.map { it.id }.toSet(),
                onFavoriteChanged = { quote, isFavorite ->
                    favorites = if (isFavorite) {
                        (favorites + quote).distinctBy { it.id }
                    } else {
                        favorites.filterNot { it.id == quote.id }
                    }
                    currentUser?.let { email ->
                        scope.launch {
                            if (isFavorite) {
                                favoritesRepository.add(email, quote)
                            } else {
                                favoritesRepository.remove(email, quote)
                            }
                        }
                    }
                },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                onLogoutClick = {
                    scope.launch {
                        appDataStore.clearSession()
                        currentUser = null
                        favorites = emptyList()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onRetryClick = { scope.launch { loadQuote() } },
                onNextQuoteClick = { scope.launch { loadQuote() } }
            )
        }
        composable(Screen.Favorites.route) {
            Favoritos(
                quotes = favorites,
                onDeleteClick = { quote ->
                    favorites = favorites.filterNot { it.id == quote.id }
                    currentUser?.let { email ->
                        scope.launch { favoritesRepository.remove(email, quote) }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

private fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}