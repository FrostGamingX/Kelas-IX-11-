// data/repository/WeatherRepository.kt
package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.RetrofitInstance

class WeatherRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "b99cbdc1357161131a93d9759d63d1ea" // atau lewat BuildConfig

    suspend fun getWeather(city: String) = api.getCurrentWeather(city, apiKey)
}