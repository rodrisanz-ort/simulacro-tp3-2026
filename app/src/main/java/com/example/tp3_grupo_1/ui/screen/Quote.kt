package com.example.tp3_grupo_1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp3_grupo_1.model.Quote
import com.example.tp3_grupo_1.ui.theme.TP3Grupo1Theme

@Composable
fun Frases(
    quotes: List<Quote> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onFavoritesClick: () -> Unit = {},
    favoriteIds: Set<String> = emptySet(),
    onFavoriteChanged: (Quote, Boolean) -> Unit = { _, _ -> },
    onRetryClick: () -> Unit = {}
) {
    var quoteNumber by rememberSaveable { mutableStateOf(0) }

    if (isLoading) {
        LoadingQuotes()
        return
    }
    if (errorMessage != null || quotes.isEmpty()) {
        ErrorQuotes(
            message = errorMessage ?: "No se encontraron frases",
            onRetryClick = onRetryClick
        )
        return
    }

    if (quoteNumber >= quotes.size) quoteNumber = 0
    val quote = quotes[quoteNumber]
    val isFavorite = quote.id in favoriteIds

    QuoteScreen(
        quote = quote,
        isFavorite = isFavorite,
        onFavoriteClick = { onFavoriteChanged(quote, !isFavorite) },
        onNextQuoteClick = {
            quoteNumber = (quoteNumber + 1) % quotes.size
        },
        onFavoritesClick = onFavoritesClick
    )
}

@Composable
private fun LoadingQuotes() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cargando frases...")
    }
}

@Composable
private fun ErrorQuotes(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onRetryClick) {
            Text("Reintentar")
        }
    }
}

@Composable
fun QuoteScreen(
    quote: Quote,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNextQuoteClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Frase del día",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = quote.category,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "\"${quote.text}\"",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "- ${quote.author}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isFavorite) "Quitar de favoritos" else "Guardar en favoritos")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onNextQuoteClick) {
                Text("Otra frase")
            }
            OutlinedButton(onClick = onFavoritesClick) {
                Text("Favoritos")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuotePreview() {
    TP3Grupo1Theme {
        Frases(
            quotes = listOf(
                Quote(
                    "1",
                    "La mejor manera de predecir el futuro es crearlo.",
                    "Peter Drucker",
                    "Motivación"
                )
            )
        )
    }
}