// NewsViewModel.kt
package com.example.newsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    var articles = mutableStateOf<List<Article>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        loadTopHeadlines()
    }

    fun loadTopHeadlines() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                articles.value = repository.getTopHeadlines()
                errorMessage.value = null
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                articles.value = repository.searchNews(query)
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}