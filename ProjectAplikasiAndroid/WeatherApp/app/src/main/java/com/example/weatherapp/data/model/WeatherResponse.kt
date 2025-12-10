// data/model/WeatherResponse.kt
package com.example.weatherapp.data.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    val sys: Sys
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Sys(
    val country: String
)