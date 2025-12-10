// MainScreen.kt
package com.example.newsapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.data.Article
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: NewsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val articles by viewModel.articles
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari berita...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    TextButton(onClick = { viewModel.searchNews(searchQuery) }) {
                        Text("Cari")
                    }
                }
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                }
                else -> {
                    LazyColumn {
                        items(articles) { article ->
                            NewsItem(article = article, onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                context.startActivity(intent)
                            })
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            if (article.urlToImage != null) {
                Image(
                    painter = rememberAsyncImagePainter(article.urlToImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = article.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.description ?: "No description",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.publishedAt.take(10),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}