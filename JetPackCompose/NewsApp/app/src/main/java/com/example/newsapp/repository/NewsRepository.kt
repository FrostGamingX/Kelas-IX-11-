// NewsRepository.kt
package com.example.newsapp.repository

import com.example.newsapp.data.Article
import com.example.newsapp.network.ApiService
import com.example.newsapp.network.RetrofitClient

class NewsRepository {
    private val apiKey = "213ba20b8cb64d8fbdb63995bd5042d0" // atau simpan di local.properties

    suspend fun getTopHeadlines(): List<Article> {
        return RetrofitClient.apiService.getTopHeadlines(apiKey = apiKey).articles
    }

    suspend fun searchNews(query: String): List<Article> {
        return RetrofitClient.apiService.searchNews(query, apiKey).articles
    }
}