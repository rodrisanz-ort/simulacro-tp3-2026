package com.example.tp3_grupo_1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp3_grupo_1.model.Quote
import com.example.tp3_grupo_1.ui.theme.TP3Grupo1Theme

@Composable
fun Favoritos(
    quotes: List<Quote> = emptyList(),
    onDeleteClick: (Quote) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    FavoritesScreen(
        quotes = quotes,
        onDeleteClick = onDeleteClick,
        onBackClick = onBackClick
    )
}

@Composable
fun FavoritesScreen(
    quotes: List<Quote>,
    onDeleteClick: (Quote) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Mis favoritos",
            modifier = Modifier.padding(start = 24.dp, top = 32.dp, end = 24.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        if (quotes.isEmpty()) {
            EmptyFavorites(onBackClick = onBackClick)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quotes, key = { it.id }) { quote ->
                    FavoriteQuoteCard(
                        quote = quote,
                        onDeleteClick = { onDeleteClick(quote) }
                    )
                }
            }
            TextButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun EmptyFavorites(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Todavía no tenés frases favoritas.",
            style = MaterialTheme.typography.bodyLarge
        )
        TextButton(onClick = onBackClick) {
            Text("Volver a frases")
        }
    }
}

@Composable
private fun FavoriteQuoteCard(
    quote: Quote,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"${quote.text}\"",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "- ${quote.author}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyFavoritesPreview() {
    TP3Grupo1Theme {
        Favoritos()
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesPreview() {
    TP3Grupo1Theme {
        Favoritos(
            quotes = listOf(
                Quote(
                    id = "1",
                    text = "La mejor manera de predecir el futuro es crearlo.",
                    author = "Peter Drucker",
                    category = "Motivación"
                )
            )
        )
    }
}