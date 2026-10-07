package com.example.tp3_grupo_1.ui.navigation

sealed class Screen(val route: String) {
    data object Login   : Screen("login")
    data object Register: Screen("register")
    data object Recover: Screen("recover")
    data object Quote: Screen("quote")
    data object Favorites: Screen("favorites")
}